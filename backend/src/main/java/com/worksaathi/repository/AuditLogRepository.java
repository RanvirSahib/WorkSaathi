package com.worksaathi.repository;

import com.worksaathi.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByAction(com.worksaathi.entity.AuditLog.AuditAction action);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
