package com.worksaathi.dto.worker;

import com.worksaathi.entity.Worker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String bio;
    private Integer experienceYears;
    private Double hourlyRate;
    private Double dailyRate;
    private String verificationStatus;
    private String availabilityStatus;
    private Double latitude;
    private Double longitude;
    private Double averageRating;
    private Integer totalReviews;
    private String profileImage;
    private LocalDateTime createdAt;
    private List<String> services;
    private Double distance; // For nearby search

    public static WorkerResponse fromEntity(Worker worker) {
        if (worker == null) return null;
        WorkerResponse response = new WorkerResponse();
        response.setId(worker.getId());
        if (worker.getUser() != null) {
            response.setUserId(worker.getUser().getId());
            response.setName(worker.getUser().getName());
            response.setEmail(worker.getUser().getEmail());
            response.setPhone(worker.getUser().getPhone());
            response.setProfileImage(worker.getUser().getProfileImage());
        }
        response.setBio(worker.getBio());
        response.setExperienceYears(worker.getExperienceYears());
        response.setHourlyRate(worker.getHourlyRate());
        response.setDailyRate(worker.getDailyRate());
        response.setVerificationStatus(worker.getVerificationStatus() != null ? worker.getVerificationStatus().name() : "PENDING");
        response.setAvailabilityStatus(worker.getAvailabilityStatus() != null ? worker.getAvailabilityStatus().name() : "AVAILABLE");
        response.setLatitude(worker.getLatitude());
        response.setLongitude(worker.getLongitude());
        response.setAverageRating(worker.getAverageRating() != null ? worker.getAverageRating() : 0.0);
        response.setTotalReviews(worker.getTotalReviews() != null ? worker.getTotalReviews() : 0);
        response.setCreatedAt(worker.getCreatedAt());
        
        List<String> serviceNames = new ArrayList<>();
        if (worker.getWorkerSkills() != null) {
            for (var skill : worker.getWorkerSkills()) {
                if (skill.getService() != null && skill.getService().getName() != null) {
                    serviceNames.add(skill.getService().getName());
                }
            }
        }
        response.setServices(serviceNames);
        
        return response;
    }
}
