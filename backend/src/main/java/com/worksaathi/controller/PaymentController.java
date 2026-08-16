package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.dto.payment.PaymentRequest;
import com.worksaathi.dto.payment.PaymentResponse;
import com.worksaathi.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management endpoints")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment created successfully", response));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get payment by job ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByJobId(@PathVariable Long jobId) {
        PaymentResponse response = paymentService.getPaymentByJobId(jobId);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved", response));
    }

    @GetMapping("/my-payments")
    @Operation(summary = "Get current user payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments() {
        List<PaymentResponse> response = paymentService.getCustomerPayments();
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved", response));
    }

    @GetMapping("/worker-payments")
    @Operation(summary = "Get worker payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getWorkerPayments() {
        List<PaymentResponse> response = paymentService.getWorkerPayments();
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved", response));
    }
}
