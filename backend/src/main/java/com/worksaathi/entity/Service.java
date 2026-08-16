package com.worksaathi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "services")
@Getter
@Setter
@ToString(exclude = {"workerSkills", "jobs"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;

    @Column
    private Double basePrice;

    @Column(nullable = false)
    private Boolean isActive = true;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerSkill> workerSkills = new HashSet<>();

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private Set<Job> jobs = new HashSet<>();

    public enum ServiceCategory {
        ELECTRICAL,
        PLUMBING,
        CARPENTRY,
        PAINTING,
        CLEANING,
        AC_REPAIR,
        MECHANIC,
        GARDENING,
        GENERAL
    }
}
