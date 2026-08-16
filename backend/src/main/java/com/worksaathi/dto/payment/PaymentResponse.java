package com.worksaathi.dto.payment;

import com.worksaathi.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long jobId;
    private Long customerId;
    private String customerName;
    private Long workerId;
    private String workerName;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setJobId(payment.getJob().getId());
        response.setCustomerId(payment.getCustomer().getId());
        response.setCustomerName(payment.getCustomer().getName());
        response.setWorkerId(payment.getWorker().getId());
        response.setWorkerName(payment.getWorker().getUser().getName());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaymentMethod(payment.getPaymentMethod().name());
        response.setPaymentStatus(payment.getPaymentStatus().name());
        response.setTransactionId(payment.getTransactionId());
        response.setCreatedAt(payment.getCreatedAt());
        response.setCompletedAt(payment.getCompletedAt());
        return response;
    }
}
