package com.worksaathi.dto.job;

import com.worksaathi.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long workerId;
    private String workerName;
    private Long serviceId;
    private String serviceName;
    private String title;
    private String description;
    private String status;
    private LocalDateTime scheduledDate;
    private String scheduledTime;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double estimatedPrice;
    private Double finalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private List<JobStatusHistory> statusHistory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobStatusHistory {
        private String status;
        private String changedBy;
        private LocalDateTime timestamp;
    }

    public static JobResponse fromEntity(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setCustomerId(job.getCustomer().getId());
        response.setCustomerName(job.getCustomer().getName());
        
        if (job.getWorker() != null) {
            response.setWorkerId(job.getWorker().getId());
            response.setWorkerName(job.getWorker().getUser().getName());
        }
        
        response.setServiceId(job.getService().getId());
        response.setServiceName(job.getService().getName());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setStatus(job.getStatus().name());
        response.setScheduledDate(job.getScheduledDate());
        response.setScheduledTime(job.getScheduledTime());
        response.setAddress(job.getAddress());
        response.setLatitude(job.getLatitude());
        response.setLongitude(job.getLongitude());
        response.setEstimatedPrice(job.getEstimatedPrice());
        response.setFinalPrice(job.getFinalPrice());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());
        response.setCompletedAt(job.getCompletedAt());
        
        if (job.getStatusHistory() != null) {
            response.setStatusHistory(job.getStatusHistory().stream()
                .map(history -> new JobStatusHistory(
                    history.getStatus().name(),
                    history.getChangedBy() != null ? history.getChangedBy().getName() : "System",
                    history.getTimestamp()
                ))
                .collect(Collectors.toList()));
        }
        
        return response;
    }
}
