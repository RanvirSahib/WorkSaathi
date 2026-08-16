package com.worksaathi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WorkSaathiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkSaathiApplication.class, args);
    }
}
