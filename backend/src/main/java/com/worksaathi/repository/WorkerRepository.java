package com.worksaathi.repository;

import com.worksaathi.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByUserId(Long userId);

    @Query("SELECT w FROM Worker w WHERE w.verificationStatus = 'APPROVED' AND w.availabilityStatus = 'AVAILABLE'")
    List<Worker> findAvailableVerifiedWorkers();

    long countByVerificationStatus(Worker.VerificationStatus status);

    List<Worker> findAllByVerificationStatus(Worker.VerificationStatus status);

    @Query("SELECT w FROM Worker w WHERE w.availabilityStatus = :status")
    List<Worker> findByAvailabilityStatus(@Param("status") Worker.AvailabilityStatus status);

    @Query(value = "SELECT w FROM Worker w JOIN w.workerSkills ws WHERE ws.service.id = :serviceId AND w.verificationStatus = 'APPROVED'",
           countQuery = "SELECT COUNT(w) FROM Worker w JOIN w.workerSkills ws WHERE ws.service.id = :serviceId AND w.verificationStatus = 'APPROVED'")
    Page<Worker> findByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);

    @Query(value = "SELECT w FROM Worker w WHERE " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(w.latitude)) * " +
           "cos(radians(w.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(w.latitude)))) < :radius " +
           "AND w.verificationStatus = 'APPROVED'",
           countQuery = "SELECT COUNT(w) FROM Worker w WHERE " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(w.latitude)) * " +
           "cos(radians(w.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(w.latitude)))) < :radius " +
           "AND w.verificationStatus = 'APPROVED'")
    Page<Worker> findNearbyWorkers(@Param("latitude") Double latitude,
                                    @Param("longitude") Double longitude,
                                    @Param("radius") Double radius,
                                    Pageable pageable);

    @Query(value = "SELECT w FROM Worker w WHERE w.averageRating >= :minRating AND w.verificationStatus = 'APPROVED'",
           countQuery = "SELECT COUNT(w) FROM Worker w WHERE w.averageRating >= :minRating AND w.verificationStatus = 'APPROVED'")
    Page<Worker> findByMinRating(@Param("minRating") Double minRating, Pageable pageable);

    Page<Worker> findByVerificationStatus(Worker.VerificationStatus status, Pageable pageable);
}
