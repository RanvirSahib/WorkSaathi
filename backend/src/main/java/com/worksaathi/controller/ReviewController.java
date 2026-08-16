package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.dto.job.ReviewResponse;
import com.worksaathi.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management endpoints")
public class ReviewController {

    private final JobService jobService;

    @PostMapping("/job/{jobId}")
    @Operation(summary = "Create a review for a job")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long jobId,
            @Valid @RequestBody com.worksaathi.dto.job.ReviewRequest request) {
        ReviewResponse response = jobService.createReview(jobId, request);
        return ResponseEntity.ok(ApiResponse.success("Review created successfully", response));
    }

    @GetMapping("/worker/{workerId}")
    @Operation(summary = "Get reviews for a worker")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getWorkerReviews(@PathVariable Long workerId) {
        // This would need to be implemented in the service
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved", List.of()));
    }
}
