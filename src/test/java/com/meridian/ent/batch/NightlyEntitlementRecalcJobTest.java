package com.meridian.ent.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meridian.ent.config.EntitlementDataConfig;
import com.meridian.ent.dao.BatchAuditDao;
import com.meridian.ent.dao.EntitlementDao;
import com.meridian.ent.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NightlyEntitlementRecalcJobTest.TestConfig.class)
@TestPropertySource(locations = "classpath:application.properties", properties = {
    "ent.jdbc.url=jdbc:h2:mem:enttest;MODE=Oracle;DB_CLOSE_DELAY=-1",
    "ent.extract.dir=target/test-classes/extracts"
})
public class NightlyEntitlementRecalcJobTest {

    @Configuration
    @Import(EntitlementDataConfig.class)
    @ComponentScan({"com.meridian.ent.batch", "com.meridian.ent.dao", "com.meridian.ent.service"})
    static class TestConfig {
    }

    @Autowired
    private NightlyEntitlementRecalcJob job;
    @Autowired
    private EntitlementDao entitlementDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BatchAuditDao batchAuditDao;

    @Test
    public void recalcFromExtract() throws Exception {
        RecalcResult r = job.recalc(new SimpleDateFormat("yyyyMMdd").parse("20240312"));

        assertEquals("OK", r.status);
        assertEquals("HR_USERS_20240311.csv", r.extractFile);
        assertEquals(4, r.usersUpdated);

        // grade dropped to 4 -> INDEX_PRELIM downgraded to READ
        assertTrue(entitlementDao.hasAccess("u10002", "INDEX_PRELIM"));
        assertTrue(entitlementDao.hasAccess("u10004", "ESG_SCORES"));
        assertTrue(entitlementDao.hasAccess("u10005", "INDEX_PRELIM"));

        // u10003 not in feed
        assertEquals("TERMINATED", userDao.findById("u10003").getStatus());
        assertFalse(entitlementDao.hasAccess("u10003", "PRICING_EOD"));

        assertEquals("OK", batchAuditDao.findLatest(NightlyEntitlementRecalcJob.JOB_NAME).getStatus());
    }
}
