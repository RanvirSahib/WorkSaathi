package com.worksaathi.repository;

import com.worksaathi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByJobId(Long jobId);

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByWorkerId(Long workerId);

    List<Payment> findByPaymentStatus(com.worksaathi.entity.Payment.PaymentStatus status);
}
