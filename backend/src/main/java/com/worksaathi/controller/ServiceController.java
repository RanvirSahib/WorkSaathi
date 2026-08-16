package com.worksaathi.controller;

import com.worksaathi.dto.ApiResponse;
import com.worksaathi.entity.Service;
import com.worksaathi.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Service>>> getServices() {
        return ResponseEntity.ok(ApiResponse.success("Services retrieved", serviceService.getAllServices()));
    }
}
