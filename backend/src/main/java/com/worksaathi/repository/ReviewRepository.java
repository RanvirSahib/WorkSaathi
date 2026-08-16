package com.worksaathi.repository;

import com.worksaathi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByJobId(Long jobId);

    List<Review> findByWorkerId(Long workerId);

    List<Review> findByCustomerId(Long customerId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.worker.id = :workerId")
    Long countByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.worker.id = :workerId")
    Double averageRatingByWorkerId(@Param("workerId") Long workerId);

    boolean existsByJobId(Long jobId);
}
