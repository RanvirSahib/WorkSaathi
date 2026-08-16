package com.worksaathi.dto.worker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerAvailabilityRequest {

    @NotNull(message = "Availability list is required")
    private List<AvailabilitySlot> availabilitySlots;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilitySlot {
        @NotBlank(message = "Day of week is required")
        private String dayOfWeek; // MONDAY, TUESDAY, etc.

        @NotBlank(message = "Start time is required")
        private String startTime; // HH:mm format

        @NotBlank(message = "End time is required")
        private String endTime; // HH:mm format

        private Boolean isAvailable = true;
    }
}
