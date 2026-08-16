package com.worksaathi.service;

import com.worksaathi.dto.worker.WorkerAvailabilityRequest;
import com.worksaathi.dto.worker.WorkerProfileRequest;
import com.worksaathi.dto.worker.WorkerResponse;
import com.worksaathi.entity.Service;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.entity.WorkerAvailability;
import com.worksaathi.entity.WorkerSkill;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.exception.UnauthorizedException;
import com.worksaathi.repository.ServiceRepository;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerAvailabilityRepository;
import com.worksaathi.repository.WorkerRepository;
import com.worksaathi.repository.WorkerSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final WorkerAvailabilityRepository workerAvailabilityRepository;

    public WorkerResponse getCurrentWorkerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        return WorkerResponse.fromEntity(worker);
    }

    @Transactional
    public WorkerResponse createWorkerProfile(WorkerProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (workerRepository.findByUserId(user.getId()).isPresent()) {
            throw new BadRequestException("Worker profile already exists");
        }

        Worker worker = new Worker();
        worker.setUser(user);
        worker.setBio(request.getBio());
        worker.setExperienceYears(request.getExperienceYears());
        worker.setHourlyRate(request.getHourlyRate());
        worker.setDailyRate(request.getDailyRate());
        worker.setLatitude(request.getLatitude());
        worker.setLongitude(request.getLongitude());
        worker.setVerificationStatus(Worker.VerificationStatus.PENDING);
        worker.setAvailabilityStatus(Worker.AvailabilityStatus.AVAILABLE);
        worker.setAverageRating(0.0);
        worker.setTotalReviews(0);

        worker = workerRepository.save(worker);

        // Add worker skills
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            for (Long serviceId : request.getServiceIds()) {
                Service service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new ResourceNotFoundException("Service", serviceId));

                WorkerSkill skill = new WorkerSkill();
                skill.setWorker(worker);
                skill.setService(service);
                skill.setExperienceLevel(WorkerSkill.ExperienceLevel.INTERMEDIATE);
                workerSkillRepository.save(skill);
            }
        }

        return WorkerResponse.fromEntity(worker);
    }

    @Transactional
    public WorkerResponse updateWorkerProfile(WorkerProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        if (request.getBio() != null) {
            worker.setBio(request.getBio());
        }
        if (request.getExperienceYears() != null) {
            worker.setExperienceYears(request.getExperienceYears());
        }
        if (request.getHourlyRate() != null) {
            worker.setHourlyRate(request.getHourlyRate());
        }
        if (request.getDailyRate() != null) {
            worker.setDailyRate(request.getDailyRate());
        }
        if (request.getLatitude() != null) {
            worker.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            worker.setLongitude(request.getLongitude());
        }

        worker = workerRepository.save(worker);

        // Update worker skills
        if (request.getServiceIds() != null) {
            // Remove existing skills
            java.util.List<WorkerSkill> existingSkills = workerSkillRepository.findByWorkerId(worker.getId());
            if (!existingSkills.isEmpty()) {
                workerSkillRepository.deleteAll(existingSkills);
            }

            // Add new skills
            for (Long serviceId : request.getServiceIds()) {
                Service service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new ResourceNotFoundException("Service", serviceId));

                WorkerSkill skill = new WorkerSkill();
                skill.setWorker(worker);
                skill.setService(service);
                skill.setExperienceLevel(WorkerSkill.ExperienceLevel.INTERMEDIATE);
                workerSkillRepository.save(skill);
            }
        }

        return WorkerResponse.fromEntity(worker);
    }

    @Transactional
    public void updateWorkerAvailability(WorkerAvailabilityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found"));

        // Remove existing availability
        java.util.List<WorkerAvailability> existingAvailability = workerAvailabilityRepository.findByWorkerId(worker.getId());
        if (!existingAvailability.isEmpty()) {
            workerAvailabilityRepository.deleteAll(existingAvailability);
        }

        // Add new availability
        for (WorkerAvailabilityRequest.AvailabilitySlot slot : request.getAvailabilitySlots()) {
            WorkerAvailability availability = new WorkerAvailability();
            availability.setWorker(worker);
            availability.setDayOfWeek(WorkerAvailability.DayOfWeek.valueOf(slot.getDayOfWeek().toUpperCase()));
            availability.setStartTime(slot.getStartTime());
            availability.setEndTime(slot.getEndTime());
            availability.setIsAvailable(slot.getIsAvailable());
            workerAvailabilityRepository.save(availability);
        }
    }

    public Page<WorkerResponse> searchWorkers(
            Long serviceId,
            Double latitude,
            Double longitude,
            Double radius,
            Double minRating,
            Pageable pageable) {

        Page<Worker> workers;

        if (latitude != null && longitude != null && radius != null) {
            // Nearby search
            workers = workerRepository.findNearbyWorkers(latitude, longitude, radius, pageable);
        } else if (serviceId != null) {
            // Search by service
            workers = workerRepository.findByServiceId(serviceId, pageable);
        } else if (minRating != null) {
            // Search by rating
            workers = workerRepository.findByMinRating(minRating, pageable);
        } else {
            // Get all verified workers
            workers = workerRepository.findByVerificationStatus(Worker.VerificationStatus.APPROVED, pageable);
        }

        return workers.map(WorkerResponse::fromEntity);
    }

    public WorkerResponse getWorkerById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", id));

        return WorkerResponse.fromEntity(worker);
    }
}
