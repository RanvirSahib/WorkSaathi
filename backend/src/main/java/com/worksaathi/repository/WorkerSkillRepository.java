package com.worksaathi.repository;

import com.worksaathi.entity.WorkerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerSkillRepository extends JpaRepository<WorkerSkill, Long> {

    List<WorkerSkill> findByWorkerId(Long workerId);

    List<WorkerSkill> findByServiceId(Long serviceId);

    boolean existsByWorkerIdAndServiceId(Long workerId, Long serviceId);
}
