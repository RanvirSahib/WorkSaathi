package com.worksaathi.service;

import com.worksaathi.dto.payment.PaymentRequest;
import com.worksaathi.dto.payment.PaymentResponse;
import com.worksaathi.entity.Job;
import com.worksaathi.entity.Notification;
import com.worksaathi.entity.Payment;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.repository.JobRepository;
import com.worksaathi.repository.PaymentRepository;
import com.worksaathi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", request.getJobId()));

        if (!job.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("You can only create payment for your own jobs");
        }

        if (job.getStatus() != Job.JobStatus.COMPLETED) {
            throw new BadRequestException("Job must be completed before payment");
        }

        if (paymentRepository.findByJobId(request.getJobId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this job");
        }

        Payment payment = new Payment();
        payment.setJob(job);
        payment.setCustomer(customer);
        payment.setWorker(job.getWorker());
        payment.setAmount(request.getAmount());
        payment.setCurrency("INR");
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId(request.getTransactionId());
        payment.setCompletedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        // Update job final price
        job.setFinalPrice(request.getAmount());
        jobRepository.save(job);

        // Notify worker
        notificationService.createNotification(
                job.getWorker().getUser(),
                "Payment Received",
                "Payment of ₹" + request.getAmount() + " received for job",
                Notification.NotificationType.PAYMENT,
                job.getId()
        );

        return PaymentResponse.fromEntity(payment);
    }

    public PaymentResponse getPaymentByJobId(Long jobId) {
        Payment payment = paymentRepository.findByJobId(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for job", jobId));

        return PaymentResponse.fromEntity(payment);
    }

    public List<PaymentResponse> getCustomerPayments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return paymentRepository.findByCustomerId(customer.getId()).stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    public List<PaymentResponse> getWorkerPayments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // This assumes the user is a worker
        // In a real app, you'd need to get the worker profile first
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getWorker().getUser().getEmail().equals(email))
                .map(PaymentResponse::fromEntity)
                .toList();
    }
}
