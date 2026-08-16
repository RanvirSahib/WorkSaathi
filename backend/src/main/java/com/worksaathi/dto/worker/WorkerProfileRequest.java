package com.worksaathi.dto.worker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerProfileRequest {

    @NotBlank(message = "Bio is required")
    private String bio;

    @NotNull(message = "Experience years is required")
    @Positive(message = "Experience years must be positive")
    private Integer experienceYears;

    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;

    @Positive(message = "Daily rate must be positive")
    private Double dailyRate;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private List<Long> serviceIds; // List of service IDs the worker offers
}
