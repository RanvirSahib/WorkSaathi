package com.worksaathi.config;

import com.worksaathi.entity.*;
import com.worksaathi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final ServiceRepository serviceRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database initialization...");
        seedServices();
        seedUsersAndWorkers();
        log.info("Database initialization completed successfully.");
    }

    private void seedServices() {
        if (serviceRepository.count() > 0) {
            return;
        }

        log.info("Seeding default services...");
        List<Service> services = List.of(
                createServiceEntity("Electrician", "Wiring, switchboard repair, appliance installation & electrical maintenance", Service.ServiceCategory.ELECTRICAL, 350.0),
                createServiceEntity("Plumber", "Leak repair, bathroom & kitchen fittings, pipe installation & drainage", Service.ServiceCategory.PLUMBING, 300.0),
                createServiceEntity("Carpenter", "Furniture repair, custom woodwork, modular fittings & door repairs", Service.ServiceCategory.CARPENTRY, 400.0),
                createServiceEntity("Home Cleaning", "Deep cleaning, kitchen & bathroom sanitation, sofa & carpet shampooing", Service.ServiceCategory.CLEANING, 250.0),
                createServiceEntity("AC Service", "AC installation, gas refill, deep cleaning & repair", Service.ServiceCategory.AC_REPAIR, 500.0),
                createServiceEntity("Painter", "Interior & exterior wall painting, waterproof coating & touch-ups", Service.ServiceCategory.PAINTING, 450.0),
                createServiceEntity("Mechanic", "Two-wheeler & four-wheeler servicing, battery & puncture repair", Service.ServiceCategory.MECHANIC, 300.0),
                createServiceEntity("Gardening", "Lawn mowing, plant pruning, landscaping & pest management", Service.ServiceCategory.GARDENING, 200.0)
        );

        serviceRepository.saveAll(services);
        log.info("Seeded {} services.", services.size());
    }

    private Service createServiceEntity(String name, String desc, Service.ServiceCategory category, Double price) {
        Service s = new Service();
        s.setName(name);
        s.setDescription(desc);
        s.setCategory(category);
        s.setBasePrice(price);
        s.setIsActive(true);
        return s;
    }

    private void seedUsersAndWorkers() {
        // Admin
        if (userRepository.findByEmail("admin@worksaathi.com").isEmpty()) {
            User admin = new User();
            admin.setName("WorkSaathi Admin");
            admin.setEmail("admin@worksaathi.com");
            admin.setPhone("+919800000001");
            admin.setPasswordHash(passwordEncoder.encode("password123"));
            admin.setRole(User.Role.ADMIN);
            admin.setIsActive(true);
            admin.setIsVerified(true);
            userRepository.save(admin);
            log.info("Seeded Admin account: admin@worksaathi.com / password123");
        }

        // Customer
        User customer = userRepository.findByEmail("customer@worksaathi.com").orElse(null);
        if (customer == null) {
            customer = new User();
            customer.setName("Ranvir Singh");
            customer.setEmail("customer@worksaathi.com");
            customer.setPhone("+919800000002");
            customer.setPasswordHash(passwordEncoder.encode("password123"));
            customer.setRole(User.Role.CUSTOMER);
            customer.setIsActive(true);
            customer.setIsVerified(true);
            customer = userRepository.save(customer);
            log.info("Seeded Customer account: customer@worksaathi.com / password123");
        }

        // Seed Sample Workers
        Service electrician = serviceRepository.findByName("Electrician").orElse(null);
        Service plumber = serviceRepository.findByName("Plumber").orElse(null);
        Service carpenter = serviceRepository.findByName("Carpenter").orElse(null);
        Service cleaning = serviceRepository.findByName("Home Cleaning").orElse(null);
        Service acService = serviceRepository.findByName("AC Service").orElse(null);

        Worker worker1 = seedWorker("Raj Kumar", "raj@worksaathi.com", "+919800000003",
                "Reliable electrician with 5+ years experience in domestic wiring and installations.",
                5, 150.0, 500.0, 4.8, 127, 28.6139, 77.2090, List.of(electrician, acService));

        Worker worker2 = seedWorker("Sanjay Verma", "sanjay@worksaathi.com", "+919800000004",
                "Expert plumber specializing in emergency leak fixes and sanitary fittings.",
                7, 120.0, 450.0, 4.9, 92, 28.6200, 77.2150, List.of(plumber));

        Worker worker3 = seedWorker("Amit Singh", "amit@worksaathi.com", "+919800000005",
                "Skilled craftsman for all woodwork, doors, modular cabinets, and repairs.",
                6, 180.0, 600.0, 4.7, 84, 28.6300, 77.2200, List.of(carpenter));

        Worker worker4 = seedWorker("Priya Sharma", "priya@worksaathi.com", "+919800000006",
                "Professional home hygiene specialist offering eco-friendly deep cleaning.",
                4, 100.0, 350.0, 4.9, 204, 28.6100, 77.2000, List.of(cleaning));

        // Seed Sample Jobs for customer
        if (jobRepository.count() == 0 && customer != null && worker1 != null && electrician != null) {
            Job job1 = new Job();
            job1.setCustomer(customer);
            job1.setWorker(worker1);
            job1.setService(electrician);
            job1.setTitle("AC repair & switchboard fix");
            job1.setDescription("AC is making a loud noise and bedroom switchboard needs repair.");
            job1.setStatus(Job.JobStatus.ON_THE_WAY);
            job1.setScheduledDate(LocalDateTime.now().plusHours(2));
            job1.setScheduledTime("4:00 PM");
            job1.setAddress("Flat 402, Green Valley Apartments, New Delhi");
            job1.setLatitude(28.6139);
            job1.setLongitude(77.2090);
            job1.setEstimatedPrice(500.0);
            jobRepository.save(job1);

            if (worker2 != null && plumber != null) {
                Job job2 = new Job();
                job2.setCustomer(customer);
                job2.setWorker(worker2);
                job2.setService(plumber);
                job2.setTitle("Kitchen pipe replacement");
                job2.setDescription("Under-sink pipe is leaking water.");
                job2.setStatus(Job.JobStatus.COMPLETED);
                job2.setScheduledDate(LocalDateTime.now().minusDays(3));
                job2.setScheduledTime("11:00 AM");
                job2.setAddress("Flat 402, Green Valley Apartments, New Delhi");
                job2.setLatitude(28.6139);
                job2.setLongitude(77.2090);
                job2.setEstimatedPrice(450.0);
                job2.setFinalPrice(450.0);
                job2.setCompletedAt(LocalDateTime.now().minusDays(3).plusHours(2));
                jobRepository.save(job2);
            }
            log.info("Seeded sample customer jobs.");
        }
    }

    private Worker seedWorker(String name, String email, String phone, String bio,
                              Integer exp, Double hourly, Double daily, Double rating, Integer reviews,
                              Double lat, Double lng, List<Service> services) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPasswordHash(passwordEncoder.encode("password123"));
            user.setRole(User.Role.WORKER);
            user.setIsActive(true);
            user.setIsVerified(true);
            user = userRepository.save(user);
        }

        User finalUser = user;
        Worker worker = workerRepository.findByUserId(user.getId()).orElseGet(() -> {
            Worker w = new Worker();
            w.setUser(finalUser);
            w.setBio(bio);
            w.setExperienceYears(exp);
            w.setHourlyRate(hourly);
            w.setDailyRate(daily);
            w.setAverageRating(rating);
            w.setTotalReviews(reviews);
            w.setLatitude(lat);
            w.setLongitude(lng);
            w.setVerificationStatus(Worker.VerificationStatus.APPROVED);
            w.setAvailabilityStatus(Worker.AvailabilityStatus.AVAILABLE);
            return workerRepository.save(w);
        });

        if (services != null) {
            for (Service s : services) {
                if (s != null && !workerSkillRepository.existsByWorkerIdAndServiceId(worker.getId(), s.getId())) {
                    WorkerSkill skill = new WorkerSkill();
                    skill.setWorker(worker);
                    skill.setService(s);
                    skill.setExperienceLevel(WorkerSkill.ExperienceLevel.EXPERT);
                    workerSkillRepository.save(skill);
                }
            }
        }

        return worker;
    }
}
