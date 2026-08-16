package com.worksaathi.repository;

import com.worksaathi.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByName(String name);

    List<Service> findByCategory(com.worksaathi.entity.Service.ServiceCategory category);

    List<Service> findByIsActiveTrue();

    boolean existsByName(String name);
}
