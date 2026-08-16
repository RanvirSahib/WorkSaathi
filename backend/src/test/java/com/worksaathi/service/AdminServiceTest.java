package com.worksaathi.service;

import com.worksaathi.entity.Report;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.repository.ReportRepository;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerDocumentRepository;
import com.worksaathi.repository.WorkerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkerDocumentRepository workerDocumentRepository;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private AdminService adminService;

    private User adminUser;
    private Worker pendingWorker;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setName("Admin");
        adminUser.setEmail("admin@worksaathi.com");
        adminUser.setRole(User.Role.ADMIN);

        User workerUser = new User();
        workerUser.setId(101L);
        workerUser.setName("Pending Pro");
        workerUser.setEmail("pending@worksaathi.com");
        workerUser.setRole(User.Role.WORKER);

        pendingWorker = new Worker();
        pendingWorker.setId(50L);
        pendingWorker.setUser(workerUser);
        pendingWorker.setVerificationStatus(Worker.VerificationStatus.PENDING);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin@worksaathi.com", "pass")
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should return platform statistics accurately")
    void testGetDashboardStats() {
        when(userRepository.count()).thenReturn(10L);
        when(workerRepository.count()).thenReturn(4L);
        when(workerRepository.countByVerificationStatus(Worker.VerificationStatus.APPROVED)).thenReturn(3L);
        when(workerRepository.countByVerificationStatus(Worker.VerificationStatus.PENDING)).thenReturn(1L);
        when(reportRepository.findByStatus(Report.ReportStatus.OPEN)).thenReturn(Collections.emptyList());

        Map<String, Object> response = adminService.getDashboardStats();

        assertNotNull(response);
        assertEquals(10L, response.get("totalUsers"));
        assertEquals(4L, response.get("totalWorkers"));
        assertEquals(3L, response.get("verifiedWorkers"));
        assertEquals(1L, response.get("pendingVerifications"));
        assertEquals(0, response.get("openReports"));
    }

    @Test
    @DisplayName("Should successfully verify a worker")
    void testVerifyWorkerSuccess() {
        when(userRepository.findByEmail("admin@worksaathi.com")).thenReturn(Optional.of(adminUser));
        when(workerRepository.findById(50L)).thenReturn(Optional.of(pendingWorker));
        when(workerRepository.save(any(Worker.class))).thenReturn(pendingWorker);
        when(workerDocumentRepository.findByWorkerId(50L)).thenReturn(Collections.emptyList());

        adminService.verifyWorker(50L);

        assertEquals(Worker.VerificationStatus.APPROVED, pendingWorker.getVerificationStatus());
        verify(workerRepository, times(1)).save(pendingWorker);
    }

    @Test
    @DisplayName("Should successfully reject a worker")
    void testRejectWorkerSuccess() {
        when(userRepository.findByEmail("admin@worksaathi.com")).thenReturn(Optional.of(adminUser));
        when(workerRepository.findById(50L)).thenReturn(Optional.of(pendingWorker));
        when(workerRepository.save(any(Worker.class))).thenReturn(pendingWorker);
        when(workerDocumentRepository.findByWorkerId(50L)).thenReturn(Collections.emptyList());

        adminService.rejectWorker(50L);

        assertEquals(Worker.VerificationStatus.REJECTED, pendingWorker.getVerificationStatus());
        verify(workerRepository, times(1)).save(pendingWorker);
    }
}
