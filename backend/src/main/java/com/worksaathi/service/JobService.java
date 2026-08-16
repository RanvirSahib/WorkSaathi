package com.worksaathi.service;

import com.worksaathi.dto.job.JobRequest;
import com.worksaathi.dto.job.JobResponse;
import com.worksaathi.dto.job.ReviewRequest;
import com.worksaathi.dto.job.ReviewResponse;
import com.worksaathi.entity.*;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.exception.UnauthorizedException;
import com.worksaathi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final ServiceRepository serviceRepository;
    private final JobStatusHistoryRepository jobStatusHistoryRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationService notificationService;

    @Transactional
    public JobResponse createJob(JobRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker", request.getWorkerId()));

        if (worker.getVerificationStatus() != Worker.VerificationStatus.APPROVED) {
            throw new BadRequestException("Worker is not verified");
        }

        if (worker.getAvailabilityStatus() != Worker.AvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("Worker is not available");
        }

        com.worksaathi.entity.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", request.getServiceId()));

        Job job = new Job();
        job.setCustomer(customer);
        job.setWorker(worker);
        job.setService(service);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setStatus(Job.JobStatus.REQUESTED);
        job.setScheduledDate(request.getScheduledDate());
        job.setScheduledTime(request.getScheduledTime());
        job.setAddress(request.getAddress());
        job.setLatitude(request.getLatitude());
        job.setLongitude(request.getLongitude());
        job.setEstimatedPrice(request.getEstimatedPrice());

        job = jobRepository.save(job);

        // Create initial status history
        createJobStatusHistory(job, Job.JobStatus.REQUESTED, customer);

        // Notify worker
        notificationService.createNotification(
                worker.getUser(),
                "New Job Request",
                "You have a new job request from " + customer.getName(),
                Notification.NotificationType.JOB_REQUEST,
                job.getId()
        );

        return JobResponse.fromEntity(job);
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", id));
        return JobResponse.fromEntity(job);
    }

    public List<JobResponse> getCustomerJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return jobRepository.findByCustomerId(customer.getId()).stream()
                .map(JobResponse::fromEntity)
                .toList();
    }

    public List<JobResponse> getWorkerJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        return jobRepository.findByWorkerId(worker.getId()).stream()
                .map(JobResponse::fromEntity)
                .toList();
    }

    public List<JobResponse> getWorkerRequestedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        return jobRepository.findRequestedJobsByWorkerId(worker.getId()).stream()
                .map(JobResponse::fromEntity)
                .toList();
    }

    @Transactional
    public JobResponse acceptJob(Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));

        if (!job.getWorker().getId().equals(worker.getId())) {
            throw new UnauthorizedException("Job", jobId);
        }

        if (job.getStatus() != Job.JobStatus.REQUESTED) {
            throw new BadRequestException("Job cannot be accepted in current status");
        }

        job.setStatus(Job.JobStatus.ACCEPTED);
        job = jobRepository.save(job);

        createJobStatusHistory(job, Job.JobStatus.ACCEPTED, user);

        // Notify customer
        notificationService.createNotification(
                job.getCustomer(),
                "Job Accepted",
                "Your job has been accepted by " + worker.getUser().getName(),
                Notification.NotificationType.JOB_ACCEPTED,
                job.getId()
        );

        return JobResponse.fromEntity(job);
    }

    @Transactional
    public JobResponse rejectJob(Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));

        if (!job.getWorker().getId().equals(worker.getId())) {
            throw new UnauthorizedException("Job", jobId);
        }

        if (job.getStatus() != Job.JobStatus.REQUESTED) {
            throw new BadRequestException("Job cannot be rejected in current status");
        }

        job.setStatus(Job.JobStatus.REJECTED);
        job = jobRepository.save(job);

        createJobStatusHistory(job, Job.JobStatus.REJECTED, user);

        // Notify customer
        notificationService.createNotification(
                job.getCustomer(),
                "Job Rejected",
                "Your job has been rejected by " + worker.getUser().getName(),
                Notification.NotificationType.SYSTEM,
                job.getId()
        );

        return JobResponse.fromEntity(job);
    }

    @Transactional
    public JobResponse updateJobStatus(Long jobId, Job.JobStatus newStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));

        if (!job.getWorker().getId().equals(worker.getId())) {
            throw new UnauthorizedException("Job", jobId);
        }

        job.setStatus(newStatus);

        if (newStatus == Job.JobStatus.COMPLETED) {
            job.setCompletedAt(LocalDateTime.now());
        }

        job = jobRepository.save(job);
        createJobStatusHistory(job, newStatus, user);

        // Notify customer based on status
        String notificationTitle = getNotificationTitleForStatus(newStatus);
        String notificationMessage = getNotificationMessageForStatus(newStatus, worker.getUser().getName());

        notificationService.createNotification(
                job.getCustomer(),
                notificationTitle,
                notificationMessage,
                Notification.NotificationType.JOB_STARTED,
                job.getId()
        );

        return JobResponse.fromEntity(job);
    }

    @Transactional
    public JobResponse cancelJob(Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));

        if (!job.getCustomer().getId().equals(user.getId())) {
            throw new UnauthorizedException("Job", jobId);
        }

        if (job.getStatus() == Job.JobStatus.COMPLETED || job.getStatus() == Job.JobStatus.CANCELLED) {
            throw new BadRequestException("Job cannot be cancelled in current status");
        }

        job.setStatus(Job.JobStatus.CANCELLED);
        job = jobRepository.save(job);

        createJobStatusHistory(job, Job.JobStatus.CANCELLED, user);

        // Notify worker
        if (job.getWorker() != null) {
            notificationService.createNotification(
                    job.getWorker().getUser(),
                    "Job Cancelled",
                    "Job has been cancelled by customer",
                    Notification.NotificationType.SYSTEM,
                    job.getId()
            );
        }

        return JobResponse.fromEntity(job);
    }

    @Transactional
    public ReviewResponse createReview(Long jobId, ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId));

        if (!job.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("Job", jobId);
        }

        if (job.getStatus() != Job.JobStatus.COMPLETED) {
            throw new BadRequestException("Job must be completed to review");
        }

        if (reviewRepository.existsByJobId(jobId)) {
            throw new BadRequestException("Review already exists for this job");
        }

        Review review = new Review();
        review.setJob(job);
        review.setCustomer(customer);
        review.setWorker(job.getWorker());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        review = reviewRepository.save(review);

        // Update worker rating
        updateWorkerRating(job.getWorker());

        return ReviewResponse.fromEntity(review);
    }

    private void createJobStatusHistory(Job job, Job.JobStatus status, User changedBy) {
        JobStatusHistory history = new JobStatusHistory();
        history.setJob(job);
        history.setStatus(status);
        history.setChangedBy(changedBy);
        jobStatusHistoryRepository.save(history);
    }

    private void updateWorkerRating(Worker worker) {
        Long reviewCount = reviewRepository.countByWorkerId(worker.getId());
        Double avgRating = reviewRepository.averageRatingByWorkerId(worker.getId());

        worker.setTotalReviews(reviewCount.intValue());
        worker.setAverageRating(avgRating != null ? avgRating : 0.0);
        workerRepository.save(worker);
    }

    private String getNotificationTitleForStatus(Job.JobStatus status) {
        return switch (status) {
            case ON_THE_WAY -> "Worker On The Way";
            case ARRIVED -> "Worker Arrived";
            case IN_PROGRESS -> "Job In Progress";
            case COMPLETED -> "Job Completed";
            default -> "Job Status Updated";
        };
    }

    private String getNotificationMessageForStatus(Job.JobStatus status, String workerName) {
        return switch (status) {
            case ON_THE_WAY -> workerName + " is on the way to your location";
            case ARRIVED -> workerName + " has arrived at your location";
            case IN_PROGRESS -> workerName + " has started working on your job";
            case COMPLETED -> "Your job has been completed by " + workerName;
            default -> "Job status has been updated";
        };
    }
}
