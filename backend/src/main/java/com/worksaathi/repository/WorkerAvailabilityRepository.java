package com.worksaathi.repository;

import com.worksaathi.entity.WorkerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerAvailabilityRepository extends JpaRepository<WorkerAvailability, Long> {

    List<WorkerAvailability> findByWorkerId(Long workerId);

    List<WorkerAvailability> findByWorkerIdAndDayOfWeek(Long workerId, com.worksaathi.entity.WorkerAvailability.DayOfWeek dayOfWeek);

    List<WorkerAvailability> findByWorkerIdAndIsAvailableTrue(Long workerId);
}
