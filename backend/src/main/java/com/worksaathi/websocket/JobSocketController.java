package com.worksaathi.websocket;

import com.worksaathi.dto.job.JobResponse;
import com.worksaathi.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class JobSocketController {

    private final JobService jobService;

    @MessageMapping("/job/{jobId}/status")
    @SendTo("/topic/job/{jobId}")
    public JobResponse updateJobStatus(
            @DestinationVariable Long jobId,
            String status) {
        
        try {
            com.worksaathi.entity.Job.JobStatus jobStatus = com.worksaathi.entity.Job.JobStatus.valueOf(status.toUpperCase());
            return jobService.updateJobStatus(jobId, jobStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid job status");
        }
    }

    @MessageMapping("/job/{jobId}/location")
    @SendTo("/topic/job/{jobId}")
    public String updateWorkerLocation(
            @DestinationVariable Long jobId,
            String locationData) {
        
        // Parse location data (latitude, longitude)
        // Update worker location in database
        // This would be implemented with proper location tracking
        
        return locationData;
    }
}
