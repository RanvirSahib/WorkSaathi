package com.worksaathi.repository;

import com.worksaathi.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCustomerId(Long customerId);

    List<Job> findByWorkerId(Long workerId);

    @Query("SELECT j FROM Job j WHERE j.worker.id = :workerId AND j.status = 'REQUESTED'")
    List<Job> findRequestedJobsByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT j FROM Job j WHERE j.worker.id = :workerId AND j.status IN ('ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS')")
    List<Job> findActiveJobsByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT j FROM Job j WHERE j.customer.id = :customerId AND j.status = 'REQUESTED'")
    List<Job> findRequestedJobsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT j FROM Job j WHERE j.customer.id = :customerId AND j.status IN ('ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS')")
    List<Job> findActiveJobsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT j FROM Job j WHERE j.status = :status")
    Page<Job> findByStatus(@Param("status") com.worksaathi.entity.Job.JobStatus status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.service.id = :serviceId")
    Page<Job> findByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.worker.id = :workerId AND j.status = 'COMPLETED'")
    List<Job> findCompletedJobsByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT j FROM Job j WHERE j.customer.id = :customerId AND j.status = 'COMPLETED'")
    List<Job> findCompletedJobsByCustomerId(@Param("customerId") Long customerId);
}
