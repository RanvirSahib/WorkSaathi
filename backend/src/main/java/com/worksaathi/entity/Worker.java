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
@Table(name = "workers")
@Getter
@Setter
@ToString(exclude = {"user", "workerSkills", "availabilitySlots"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column
    private Integer experienceYears;

    @Column
    private Double hourlyRate;

    @Column
    private Double dailyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Double averageRating = 0.0;

    @Column
    private Integer totalReviews = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.Set<WorkerSkill> workerSkills = new java.util.HashSet<>();

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<WorkerAvailability> availabilitySlots = new java.util.HashSet<>();

    public enum VerificationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum AvailabilityStatus {
        AVAILABLE,
        BUSY,
        OFFLINE
    }
}
