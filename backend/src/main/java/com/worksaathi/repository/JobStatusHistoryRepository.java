package com.worksaathi.repository;

import com.worksaathi.entity.JobStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobStatusHistoryRepository extends JpaRepository<JobStatusHistory, Long> {

    List<JobStatusHistory> findByJobIdOrderByTimestampAsc(Long jobId);
}
