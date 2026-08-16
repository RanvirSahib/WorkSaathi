package com.worksaathi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column
    private String entityType;

    @Column
    private Long entityId;

    @Column
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public enum AuditAction {
        ADMIN_APPROVED_WORKER,
        ADMIN_REJECTED_WORKER,
        USER_LOGIN,
        USER_LOGOUT,
        USER_REGISTER,
        JOB_CREATED,
        JOB_ACCEPTED,
        JOB_REJECTED,
        JOB_CANCELLED,
        JOB_COMPLETED,
        PAYMENT_COMPLETED,
        PAYMENT_FAILED,
        ACCOUNT_SUSPENDED,
        ACCOUNT_REACTIVATED,
        REVIEW_CREATED,
        REPORT_CREATED,
        REPORT_RESOLVED
    }
}
