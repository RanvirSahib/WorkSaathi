package com.worksaathi.service;

import com.worksaathi.entity.Report;
import com.worksaathi.entity.User;
import com.worksaathi.entity.Worker;
import com.worksaathi.entity.WorkerDocument;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.repository.ReportRepository;
import com.worksaathi.repository.UserRepository;
import com.worksaathi.repository.WorkerDocumentRepository;
import com.worksaathi.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final WorkerDocumentRepository workerDocumentRepository;
    private final ReportRepository reportRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalWorkers", workerRepository.count());
        stats.put("verifiedWorkers", workerRepository.countByVerificationStatus(Worker.VerificationStatus.APPROVED));
        stats.put("pendingVerifications", workerRepository.countByVerificationStatus(Worker.VerificationStatus.PENDING));
        stats.put("openReports", reportRepository.findByStatus(Report.ReportStatus.OPEN).size());
        return stats;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<Worker> getAllWorkers(Pageable pageable) {
        return workerRepository.findAll(pageable);
    }

    public List<Worker> getPendingVerifications() {
        return workerRepository.findAllByVerificationStatus(Worker.VerificationStatus.PENDING);
    }

    @Transactional
    public void verifyWorker(Long workerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId));

        worker.setVerificationStatus(Worker.VerificationStatus.APPROVED);
        workerRepository.save(worker);

        // Update all worker documents as approved
        List<WorkerDocument> documents = workerDocumentRepository.findByWorkerId(workerId);
        documents.forEach(doc -> {
            doc.setVerificationStatus(WorkerDocument.VerificationStatus.APPROVED);
            doc.setVerifiedBy(admin);
            doc.setVerifiedAt(LocalDateTime.now());
        });
        workerDocumentRepository.saveAll(documents);
    }

    @Transactional
    public void rejectWorker(Long workerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId));

        worker.setVerificationStatus(Worker.VerificationStatus.REJECTED);
        workerRepository.save(worker);

        // Update all worker documents as rejected
        List<WorkerDocument> documents = workerDocumentRepository.findByWorkerId(workerId);
        documents.forEach(doc -> {
            doc.setVerificationStatus(WorkerDocument.VerificationStatus.REJECTED);
            doc.setVerifiedBy(admin);
            doc.setVerifiedAt(LocalDateTime.now());
        });
        workerDocumentRepository.saveAll(documents);
    }

    @Transactional
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setIsActive(true);
        userRepository.save(user);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getOpenReports() {
        return reportRepository.findByStatusOrderByCreatedAtAsc(Report.ReportStatus.OPEN);
    }

    @Transactional
    public void resolveReport(Long reportId, String resolutionNotes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", reportId));

        report.setStatus(Report.ReportStatus.RESOLVED);
        report.setResolvedBy(admin);
        report.setResolvedAt(LocalDateTime.now());
        report.setResolutionNotes(resolutionNotes);

        reportRepository.save(report);
    }
}
