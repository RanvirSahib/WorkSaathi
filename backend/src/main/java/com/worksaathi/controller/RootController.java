package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "Root", description = "Root & API information endpoints")
public class RootController {

    @GetMapping({"/", "/api/v1", "/api/v1/"})
    @Operation(summary = "Get API Overview and Health Status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getApiOverview() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", "WorkSaathi REST API");
        info.put("version", "1.0.0");
        info.put("status", "UP & RUNNING");
        info.put("documentation", "http://localhost:8080/swagger-ui/index.html");
        
        Map<String, String> publicEndpoints = new LinkedHashMap<>();
        publicEndpoints.put("services", "GET /api/v1/services");
        publicEndpoints.put("workers", "GET /api/v1/workers");
        publicEndpoints.put("login", "POST /api/v1/auth/login");
        publicEndpoints.put("register", "POST /api/v1/auth/register");
        publicEndpoints.put("swaggerUI", "GET /swagger-ui/index.html");
        
        info.put("publicEndpoints", publicEndpoints);

        Map<String, String> authenticatedEndpoints = new LinkedHashMap<>();
        authenticatedEndpoints.put("customerJobs", "GET /api/v1/jobs/customer");
        authenticatedEndpoints.put("createJob", "POST /api/v1/jobs");
        authenticatedEndpoints.put("workerJobs", "GET /api/v1/jobs/worker");
        authenticatedEndpoints.put("workerRequestedJobs", "GET /api/v1/jobs/worker/requested");
        authenticatedEndpoints.put("adminDashboard", "GET /api/v1/admin/dashboard");
        
        info.put("authenticatedEndpoints", authenticatedEndpoints);

        return ResponseEntity.ok(ApiResponse.success("WorkSaathi Backend API is online", info));
    }
}
