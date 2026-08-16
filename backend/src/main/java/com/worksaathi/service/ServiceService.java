package com.worksaathi.service;

import com.worksaathi.entity.Service;
import com.worksaathi.exception.BadRequestException;
import com.worksaathi.exception.ResourceNotFoundException;
import com.worksaathi.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findByIsActiveTrue();
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));
    }

    public List<Service> getServicesByCategory(Service.ServiceCategory category) {
        return serviceRepository.findByCategory(category);
    }

    @Transactional
    public Service createService(Service service) {
        if (serviceRepository.existsByName(service.getName())) {
            throw new BadRequestException("Service with this name already exists");
        }
        return serviceRepository.save(service);
    }

    @Transactional
    public Service updateService(Long id, Service service) {
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        if (!existingService.getName().equals(service.getName()) && 
            serviceRepository.existsByName(service.getName())) {
            throw new BadRequestException("Service with this name already exists");
        }

        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setCategory(service.getCategory());
        existingService.setBasePrice(service.getBasePrice());
        existingService.setIsActive(service.getIsActive());

        return serviceRepository.save(existingService);
    }

    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        service.setIsActive(false);
        serviceRepository.save(service);
    }
}
