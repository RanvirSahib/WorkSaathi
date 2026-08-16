package com.worksaathi.repository;

import com.worksaathi.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportedById(Long reportedById);

    List<Report> findByReportedUserId(Long reportedUserId);

    List<Report> findByStatus(com.worksaathi.entity.Report.ReportStatus status);

    List<Report> findByStatusOrderByCreatedAtAsc(com.worksaathi.entity.Report.ReportStatus status);
}
