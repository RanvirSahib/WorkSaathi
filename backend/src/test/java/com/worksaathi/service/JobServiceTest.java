package com.worksaathi.service;

import com.worksaathi.dto.job.JobRequest;
import com.worksaathi.dto.job.JobResponse;
import com.worksaathi.entity.*;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.UnauthorizedException;
import com.worksaathi.repository.*;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private JobStatusHistoryRepository jobStatusHistoryRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private JobService jobService;

    private User customerUser;
    private User workerUser;
    private Worker worker;
    private Service service;
    private Job job;

    @BeforeEach
    void setUp() {
        customerUser = new User();
        customerUser.setId(10L);
        customerUser.setName("Customer");
        customerUser.setEmail("customer@test.com");
        customerUser.setRole(User.Role.CUSTOMER);

        workerUser = new User();
        workerUser.setId(20L);
        workerUser.setName("Worker");
        workerUser.setEmail("worker@test.com");
        workerUser.setRole(User.Role.WORKER);

        worker = new Worker();
        worker.setId(100L);
        worker.setUser(workerUser);
        worker.setVerificationStatus(Worker.VerificationStatus.APPROVED);
        worker.setAvailabilityStatus(Worker.AvailabilityStatus.AVAILABLE);

        service = new Service();
        service.setId(1L);
        service.setName("Electrician");
        service.setBasePrice(150.0);

        job = new Job();
        job.setId(500L);
        job.setCustomer(customerUser);
        job.setWorker(worker);
        job.setService(service);
        job.setTitle("Ceiling Fan Repair");
        job.setDescription("Fix noisy ceiling fan");
        job.setAddress("123 Street, Delhi");
        job.setStatus(Job.JobStatus.REQUESTED);
        job.setEstimatedPrice(500.0);
        job.setCreatedAt(LocalDateTime.now());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("customer@test.com", "pass")
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should successfully create a job for customer")
    void testCreateJobSuccess() {
        JobRequest request = new JobRequest();
        request.setWorkerId(100L);
        request.setServiceId(1L);
        request.setTitle("Ceiling Fan Repair");
        request.setDescription("Fix noisy ceiling fan");
        request.setAddress("123 Street, Delhi");
        request.setScheduledDate(LocalDateTime.now().plusDays(1));
        request.setScheduledTime("10:00 AM");
        request.setEstimatedPrice(500.0);

        when(userRepository.findByEmail("customer@test.com")).thenReturn(Optional.of(customerUser));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(workerRepository.findById(100L)).thenReturn(Optional.of(worker));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> {
            Job saved = invocation.getArgument(0);
            saved.setId(500L);
            return saved;
        });

        JobResponse response = jobService.createJob(request);

        assertNotNull(response);
        assertEquals("Ceiling Fan Repair", response.getTitle());
        assertEquals("REQUESTED", response.getStatus());
        verify(jobRepository, times(1)).save(any(Job.class));
        verify(jobStatusHistoryRepository, times(1)).save(any(JobStatusHistory.class));
    }

    @Test
    @DisplayName("Should transition status: REQUESTED -> ACCEPTED")
    void testWorkerAcceptJobSuccess() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("worker@test.com", "pass")
        );

        when(userRepository.findByEmail("worker@test.com")).thenReturn(Optional.of(workerUser));
        when(workerRepository.findByUserId(workerUser.getId())).thenReturn(Optional.of(worker));
        when(jobRepository.findById(500L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobResponse response = jobService.acceptJob(500L);

        assertNotNull(response);
        assertEquals("ACCEPTED", response.getStatus());
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    @DisplayName("Should transition status: ACCEPTED -> ON_THE_WAY")
    void testWorkerOnTheWaySuccess() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("worker@test.com", "pass")
        );
        job.setStatus(Job.JobStatus.ACCEPTED);

        when(userRepository.findByEmail("worker@test.com")).thenReturn(Optional.of(workerUser));
        when(workerRepository.findByUserId(workerUser.getId())).thenReturn(Optional.of(worker));
        when(jobRepository.findById(500L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobResponse response = jobService.updateJobStatus(500L, Job.JobStatus.ON_THE_WAY);

        assertEquals("ON_THE_WAY", response.getStatus());
    }

    @Test
    @DisplayName("Should transition status: IN_PROGRESS -> COMPLETED")
    void testWorkerCompleteJobSuccess() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("worker@test.com", "pass")
        );
        job.setStatus(Job.JobStatus.IN_PROGRESS);

        when(userRepository.findByEmail("worker@test.com")).thenReturn(Optional.of(workerUser));
        when(workerRepository.findByUserId(workerUser.getId())).thenReturn(Optional.of(worker));
        when(jobRepository.findById(500L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobResponse response = jobService.updateJobStatus(500L, Job.JobStatus.COMPLETED);

        assertEquals("COMPLETED", response.getStatus());
        assertNotNull(job.getCompletedAt());
    }

    @Test
    @DisplayName("Should allow customer to cancel a REQUESTED job")
    void testCustomerCancelJobSuccess() {
        job.setStatus(Job.JobStatus.REQUESTED);

        when(userRepository.findByEmail("customer@test.com")).thenReturn(Optional.of(customerUser));
        when(jobRepository.findById(500L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        JobResponse response = jobService.cancelJob(500L);

        assertEquals("CANCELLED", response.getStatus());
    }

    @Test
    @DisplayName("Should disallow another worker from accepting someone else's job")
    void testUnauthorizedWorkerCannotAcceptJob() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("other@test.com", "pass")
        );
        User otherUser = new User();
        otherUser.setId(999L);
        otherUser.setEmail("other@test.com");

        Worker otherWorker = new Worker();
        otherWorker.setId(999L);
        otherWorker.setUser(otherUser);

        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));
        when(workerRepository.findByUserId(999L)).thenReturn(Optional.of(otherWorker));
        when(jobRepository.findById(500L)).thenReturn(Optional.of(job));

        assertThrows(UnauthorizedException.class, () -> jobService.acceptJob(500L));
    }
}
