package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.dto.job.JobRequest;
import com.worksaathi.dto.job.JobResponse;
import com.worksaathi.entity.Job;
import com.worksaathi.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job management endpoints")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Create a new job")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.createJob(request);
        return ResponseEntity.ok(ApiResponse.success("Job created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get customer jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getCustomerJobs() {
        List<JobResponse> response = jobService.getCustomerJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved", response));
    }

    @GetMapping("/customer")
    @Operation(summary = "Get customer jobs explicit")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getCustomerJobsExplicit() {
        List<JobResponse> response = jobService.getCustomerJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved", response));
    }

    @GetMapping("/worker")
    @Operation(summary = "Get worker jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getWorkerJobs() {
        List<JobResponse> response = jobService.getWorkerJobs();
        return ResponseEntity.ok(ApiResponse.success("Worker jobs retrieved", response));
    }

    @GetMapping("/worker/requested")
    @Operation(summary = "Get worker pending requested jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getWorkerRequestedJobs() {
        List<JobResponse> response = jobService.getWorkerRequestedJobs();
        return ResponseEntity.ok(ApiResponse.success("Worker requested jobs retrieved", response));
    }

    @GetMapping("/{id:[0-9]+}")
    @Operation(summary = "Get job by ID")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        JobResponse response = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job retrieved", response));
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept a job")
    public ResponseEntity<ApiResponse<JobResponse>> acceptJob(@PathVariable Long id) {
        JobResponse response = jobService.acceptJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job accepted successfully", response));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a job")
    public ResponseEntity<ApiResponse<JobResponse>> rejectJob(@PathVariable Long id) {
        JobResponse response = jobService.rejectJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job rejected successfully", response));
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start a job")
    public ResponseEntity<ApiResponse<JobResponse>> startJob(@PathVariable Long id) {
        JobResponse response = jobService.updateJobStatus(id, Job.JobStatus.IN_PROGRESS);
        return ResponseEntity.ok(ApiResponse.success("Job started successfully", response));
    }

    @PostMapping("/{id}/on-the-way")
    @Operation(summary = "Mark worker as on the way")
    public ResponseEntity<ApiResponse<JobResponse>> onTheWay(@PathVariable Long id) {
        JobResponse response = jobService.updateJobStatus(id, Job.JobStatus.ON_THE_WAY);
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }

    @PostMapping("/{id}/arrived")
    @Operation(summary = "Mark worker as arrived")
    public ResponseEntity<ApiResponse<JobResponse>> arrived(@PathVariable Long id) {
        JobResponse response = jobService.updateJobStatus(id, Job.JobStatus.ARRIVED);
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete a job")
    public ResponseEntity<ApiResponse<JobResponse>> completeJob(@PathVariable Long id) {
        JobResponse response = jobService.updateJobStatus(id, Job.JobStatus.COMPLETED);
        return ResponseEntity.ok(ApiResponse.success("Job completed successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a job")
    public ResponseEntity<ApiResponse<JobResponse>> cancelJob(@PathVariable Long id) {
        JobResponse response = jobService.cancelJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job cancelled successfully", response));
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "Review a completed job")
    public ResponseEntity<ApiResponse<Object>> reviewJob(@PathVariable Long id, @Valid @RequestBody com.worksaathi.dto.job.ReviewRequest request) {
        var response = jobService.createReview(id, request);
        return ResponseEntity.ok(ApiResponse.success("Review created successfully", response));
    }
}
