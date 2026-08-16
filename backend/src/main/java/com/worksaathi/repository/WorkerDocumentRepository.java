package com.worksaathi.repository;

import com.worksaathi.entity.WorkerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerDocumentRepository extends JpaRepository<WorkerDocument, Long> {

    List<WorkerDocument> findByWorkerId(Long workerId);

    List<WorkerDocument> findByVerificationStatus(com.worksaathi.entity.WorkerDocument.VerificationStatus status);
}
