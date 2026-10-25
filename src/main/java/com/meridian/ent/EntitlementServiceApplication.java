package com.meridian.ent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EntitlementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntitlementServiceApplication.class, args);
    }
}
