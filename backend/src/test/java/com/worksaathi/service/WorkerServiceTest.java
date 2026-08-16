package com.worksaathi.service;

import com.worksaathi.dto.worker.WorkerAvailabilityRequest;
import com.worksaathi.dto.worker.WorkerResponse;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.entity.WorkerAvailability;
import com.worksaathi.repository.ServiceRepository;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerAvailabilityRepository;
import com.worksaathi.repository.WorkerRepository;
import com.worksaathi.repository.WorkerSkillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private WorkerSkillRepository workerSkillRepository;

    @Mock
    private WorkerAvailabilityRepository workerAvailabilityRepository;

    @InjectMocks
    private WorkerService workerService;

    private User workerUser;
    private Worker worker;

    @BeforeEach
    void setUp() {
        workerUser = new User();
        workerUser.setId(10L);
        workerUser.setName("Raj Kumar");
        workerUser.setEmail("raj@test.com");
        workerUser.setRole(User.Role.WORKER);

        worker = new Worker();
        worker.setId(100L);
        worker.setUser(workerUser);
        worker.setBio("Professional electrician");
        worker.setExperienceYears(5);
        worker.setHourlyRate(100.0);
        worker.setDailyRate(500.0);
        worker.setVerificationStatus(Worker.VerificationStatus.APPROVED);
        worker.setAvailabilityStatus(Worker.AvailabilityStatus.AVAILABLE);
        worker.setAverageRating(4.8);
        worker.setTotalReviews(20);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("raj@test.com", "pass")
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should retrieve current worker profile")
    void testGetCurrentWorkerProfile() {
        when(userRepository.findByEmail("raj@test.com")).thenReturn(Optional.of(workerUser));
        when(workerRepository.findByUserId(10L)).thenReturn(Optional.of(worker));

        WorkerResponse response = workerService.getCurrentWorkerProfile();

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Raj Kumar", response.getName());
        assertEquals(5, response.getExperienceYears());
        assertEquals("AVAILABLE", response.getAvailabilityStatus());
    }

    @Test
    @DisplayName("Should update worker availability schedule slots")
    void testUpdateWorkerAvailability() {
        WorkerAvailabilityRequest request = new WorkerAvailabilityRequest();
        WorkerAvailabilityRequest.AvailabilitySlot slot = new WorkerAvailabilityRequest.AvailabilitySlot();
        slot.setDayOfWeek("MONDAY");
        slot.setStartTime("09:00");
        slot.setEndTime("17:00");
        slot.setIsAvailable(true);
        request.setAvailabilitySlots(List.of(slot));

        when(userRepository.findByEmail("raj@test.com")).thenReturn(Optional.of(workerUser));
        when(workerRepository.findByUserId(10L)).thenReturn(Optional.of(worker));
        when(workerAvailabilityRepository.findByWorkerId(100L)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> workerService.updateWorkerAvailability(request));
        verify(workerAvailabilityRepository, times(1)).save(any(WorkerAvailability.class));
    }

    @Test
    @DisplayName("Should search verified workers page")
    void testSearchWorkers() {
        Page<Worker> page = new PageImpl<>(List.of(worker));
        when(workerRepository.findByVerificationStatus(eq(Worker.VerificationStatus.APPROVED), any())).thenReturn(page);

        Page<WorkerResponse> result = workerService.searchWorkers(null, null, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Raj Kumar", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Should get worker by id")
    void testGetWorkerById() {
        when(workerRepository.findById(100L)).thenReturn(Optional.of(worker));

        WorkerResponse response = workerService.getWorkerById(100L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Raj Kumar", response.getName());
    }
}
