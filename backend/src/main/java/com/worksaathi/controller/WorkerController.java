package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.dto.worker.WorkerAvailabilityRequest;
import com.worksaathi.dto.worker.WorkerProfileRequest;
import com.worksaathi.dto.worker.WorkerResponse;
import com.worksaathi.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
@Tag(name = "Workers", description = "Worker management endpoints")
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping("/me")
    @Operation(summary = "Get current worker profile")
    public ResponseEntity<ApiResponse<WorkerResponse>> getCurrentWorkerProfile() {
        WorkerResponse response = workerService.getCurrentWorkerProfile();
        return ResponseEntity.ok(ApiResponse.success("Worker profile retrieved", response));
    }

    @PostMapping("/profile")
    @Operation(summary = "Create worker profile")
    public ResponseEntity<ApiResponse<WorkerResponse>> createWorkerProfile(@Valid @RequestBody WorkerProfileRequest request) {
        WorkerResponse response = workerService.createWorkerProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Worker profile created successfully", response));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update worker profile")
    public ResponseEntity<ApiResponse<WorkerResponse>> updateWorkerProfile(@Valid @RequestBody WorkerProfileRequest request) {
        WorkerResponse response = workerService.updateWorkerProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Worker profile updated successfully", response));
    }

    @PutMapping("/availability")
    @Operation(summary = "Update worker availability")
    public ResponseEntity<ApiResponse<Void>> updateWorkerAvailability(@Valid @RequestBody WorkerAvailabilityRequest request) {
        workerService.updateWorkerAvailability(request);
        return ResponseEntity.ok(ApiResponse.success("Availability updated successfully", null));
    }

    @GetMapping
    @Operation(summary = "Search workers")
    public ResponseEntity<ApiResponse<Page<WorkerResponse>>> searchWorkers(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<WorkerResponse> response = workerService.searchWorkers(serviceId, latitude, longitude, radius, minRating, pageable);
        return ResponseEntity.ok(ApiResponse.success("Workers retrieved", response));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find nearby workers")
    public ResponseEntity<ApiResponse<Page<WorkerResponse>>> findNearbyWorkers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("distance").ascending());
        Page<WorkerResponse> response = workerService.searchWorkers(null, latitude, longitude, radius, null, pageable);
        return ResponseEntity.ok(ApiResponse.success("Nearby workers retrieved", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get worker by ID")
    public ResponseEntity<ApiResponse<WorkerResponse>> getWorkerById(@PathVariable Long id) {
        WorkerResponse response = workerService.getWorkerById(id);
        return ResponseEntity.ok(ApiResponse.success("Worker retrieved", response));
    }
}
