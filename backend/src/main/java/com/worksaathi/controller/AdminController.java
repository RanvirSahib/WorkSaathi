package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.entity.Report;
import com.worksaathi.entity.Service;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.service.AdminService;
import com.worksaathi.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {

    private final AdminService adminService;
    private final ServiceService serviceService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", users));
    }

    @GetMapping("/workers")
    @Operation(summary = "Get all workers")
    public ResponseEntity<ApiResponse<Page<Worker>>> getAllWorkers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Worker> workers = adminService.getAllWorkers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Workers retrieved", workers));
    }

    @GetMapping({"/workers/pending", "/workers/pending-verification"})
    @Operation(summary = "Get workers pending verification")
    public ResponseEntity<ApiResponse<List<com.worksaathi.dto.worker.WorkerResponse>>> getPendingVerifications() {
        List<com.worksaathi.dto.worker.WorkerResponse> workers = adminService.getPendingVerifications().stream()
                .map(com.worksaathi.dto.worker.WorkerResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Pending verifications retrieved", workers));
    }

    @RequestMapping(value = "/workers/{id}/verify", method = {RequestMethod.PUT, RequestMethod.POST})
    @Operation(summary = "Verify a worker")
    public ResponseEntity<ApiResponse<Void>> verifyWorker(@PathVariable Long id) {
        adminService.verifyWorker(id);
        return ResponseEntity.ok(ApiResponse.success("Worker verified successfully", null));
    }

    @RequestMapping(value = "/workers/{id}/reject", method = {RequestMethod.PUT, RequestMethod.POST})
    @Operation(summary = "Reject a worker")
    public ResponseEntity<ApiResponse<Void>> rejectWorker(@PathVariable Long id) {
        adminService.rejectWorker(id);
        return ResponseEntity.ok(ApiResponse.success("Worker rejected successfully", null));
    }

    @PutMapping("/users/{id}/suspend")
    @Operation(summary = "Suspend a user")
    public ResponseEntity<ApiResponse<Void>> suspendUser(@PathVariable Long id) {
        adminService.suspendUser(id);
        return ResponseEntity.ok(ApiResponse.success("User suspended successfully", null));
    }

    @PutMapping("/users/{id}/activate")
    @Operation(summary = "Activate a user")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        adminService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }

    @GetMapping("/reports")
    @Operation(summary = "Get all reports")
    public ResponseEntity<ApiResponse<List<Report>>> getAllReports() {
        List<Report> reports = adminService.getAllReports();
        return ResponseEntity.ok(ApiResponse.success("Reports retrieved", reports));
    }

    @GetMapping("/reports/open")
    @Operation(summary = "Get open reports")
    public ResponseEntity<ApiResponse<List<Report>>> getOpenReports() {
        List<Report> reports = adminService.getOpenReports();
        return ResponseEntity.ok(ApiResponse.success("Open reports retrieved", reports));
    }

    @PutMapping("/reports/{id}/resolve")
    @Operation(summary = "Resolve a report")
    public ResponseEntity<ApiResponse<Void>> resolveReport(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        adminService.resolveReport(id, request.get("resolutionNotes"));
        return ResponseEntity.ok(ApiResponse.success("Report resolved successfully", null));
    }

    @GetMapping("/services")
    @Operation(summary = "Get all services")
    public ResponseEntity<ApiResponse<List<Service>>> getAllServices() {
        List<Service> services = serviceService.getAllServices();
        return ResponseEntity.ok(ApiResponse.success("Services retrieved", services));
    }

    @PostMapping("/services")
    @Operation(summary = "Create a service")
    public ResponseEntity<ApiResponse<Service>> createService(@RequestBody Service service) {
        Service createdService = serviceService.createService(service);
        return ResponseEntity.ok(ApiResponse.success("Service created successfully", createdService));
    }

    @PutMapping("/services/{id}")
    @Operation(summary = "Update a service")
    public ResponseEntity<ApiResponse<Service>> updateService(@PathVariable Long id, @RequestBody Service service) {
        Service updatedService = serviceService.updateService(id, service);
        return ResponseEntity.ok(ApiResponse.success("Service updated successfully", updatedService));
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Delete a service")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully", null));
    }
}
