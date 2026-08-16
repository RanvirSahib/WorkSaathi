package com.worksaathi.repository;

import com.worksaathi.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByCustomerIdAndWorkerId(Long customerId, Long workerId);

    java.util.List<Conversation> findByCustomerId(Long customerId);

    java.util.List<Conversation> findByWorkerId(Long workerId);
}
