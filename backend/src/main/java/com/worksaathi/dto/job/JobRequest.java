package com.worksaathi.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private String scheduledTime;

    @NotBlank(message = "Address is required")
    private String address;

    private Double latitude;

    private Double longitude;

    @Positive(message = "Estimated price must be positive")
    private Double estimatedPrice;
}
