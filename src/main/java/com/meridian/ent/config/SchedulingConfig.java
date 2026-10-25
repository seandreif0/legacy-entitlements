package com.meridian.ent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

// no scheduler in a manual rerun, so it can't start a second recalc alongside it
@Configuration
@Profile("!rerun")
@EnableScheduling
public class SchedulingConfig {
}
