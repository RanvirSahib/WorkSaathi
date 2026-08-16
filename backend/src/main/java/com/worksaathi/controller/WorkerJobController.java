package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.dto.job.JobResponse;
import com.worksaathi.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/worker/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WORKER', 'ADMIN')")
@Tag(name = "Worker Jobs", description = "Worker job management endpoints")
public class WorkerJobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "Get all worker jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getWorkerJobs() {
        List<JobResponse> response = jobService.getWorkerJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved", response));
    }

    @GetMapping("/requests")
    @Operation(summary = "Get worker job requests")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getWorkerRequestedJobs() {
        List<JobResponse> response = jobService.getWorkerRequestedJobs();
        return ResponseEntity.ok(ApiResponse.success("Job requests retrieved", response));
    }
}
