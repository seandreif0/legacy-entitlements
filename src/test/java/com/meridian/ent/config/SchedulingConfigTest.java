package com.meridian.ent.config;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meridian.ent.EntitlementServiceApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EntitlementServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("local")
public class SchedulingConfigTest {

    @Autowired
    private List<ScheduledTaskHolder> holders;

    @Test
    public void nightlyRecalcScheduledAt0215() {
        List<String> crons = holders.stream()
            .flatMap(h -> h.getScheduledTasks().stream())
            .filter(t -> t.getTask() instanceof CronTask)
            .map(t -> ((CronTask) t.getTask()).getExpression())
            .collect(Collectors.toList());
        assertEquals(List.of("0 15 2 * * *"), crons);
    }
}
