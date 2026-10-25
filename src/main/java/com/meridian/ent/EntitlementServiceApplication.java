package com.meridian.ent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.meridian.ent.batch.BatchLauncher;

@SpringBootApplication
public class EntitlementServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EntitlementServiceApplication.class, args);
        if (ctx.getEnvironment().matchesProfiles(BatchLauncher.PROFILE)) {
            System.exit(SpringApplication.exit(ctx));
        }
    }
}
