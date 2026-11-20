package com.meridian.ent.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.meridian.ent.dao.BatchAuditDao;
import com.meridian.ent.dao.EntitlementDao;
import com.meridian.ent.dao.UserDao;

@SpringJUnitConfig(locations = "classpath:test-context.xml")
public class NightlyEntitlementRecalcJobTest {

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
        RecalcResult r = job.recalc(LocalDate.of(2024, 3, 12));

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
