package com.meridian.ent.batch;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.meridian.ent.dao.BatchAuditDao;
import com.meridian.ent.dao.EntitlementDao;
import com.meridian.ent.dao.UserDao;
import com.meridian.ent.model.BatchRun;
import com.meridian.ent.model.Entitlement;
import com.meridian.ent.model.HrExtractRow;
import com.meridian.ent.model.InternalUser;

/**
 * Nightly job. Do not change the schedule without telling the Index desk.
 */
@Component
public class NightlyEntitlementRecalcJob {

    public static final String JOB_NAME = "ENT_NIGHTLY_RECALC";

    private static final Logger LOG = LoggerFactory.getLogger(NightlyEntitlementRecalcJob.class);

    private final ExtractLocator extractLocator;
    private final HrExtractReader reader;
    private final EntitlementRuleEngine ruleEngine;
    private final UserDao userDao;
    private final EntitlementDao entitlementDao;
    private final BatchAuditDao batchAuditDao;

    public NightlyEntitlementRecalcJob(ExtractLocator extractLocator, HrExtractReader reader,
                                       EntitlementRuleEngine ruleEngine, UserDao userDao,
                                       EntitlementDao entitlementDao, BatchAuditDao batchAuditDao) {
        this.extractLocator = extractLocator;
        this.reader = reader;
        this.ruleEngine = ruleEngine;
        this.userDao = userDao;
        this.entitlementDao = entitlementDao;
        this.batchAuditDao = batchAuditDao;
    }

    // runs after the HR feed lands (01:00)
    @Scheduled(cron = "${ent.batch.cron}")
    public void run() {
        recalc(new Date());
    }

    public RecalcResult recalc(Date runDate) {
        RecalcResult res = new RecalcResult();
        BatchRun audit = new BatchRun();
        audit.setRunId(batchAuditDao.start(JOB_NAME));

        try {
            File extract = extractLocator.locate(runDate);
            if (extract == null) {
                LOG.error("no HR extract found");
                res.status = "NO_FILE";
                return res;
            }
            res.extractFile = extract.getName();

            List<HrExtractRow> rows = reader.read(extract);
            res.rowsRead = rows.size();
            res.rowsRejected = reader.getRejected();

            Set<String> seen = new HashSet<String>();
            for (HrExtractRow row : rows) {
                if (!row.isActive()) {
                    continue;
                }
                seen.add(row.empId);
                userDao.upsert(new InternalUser(row.empId, row.fullName, row.dept, row.costCenter, row.grade));

                entitlementDao.deleteBatchGrants(row.empId);
                for (Entitlement e : ruleEngine.derive(row)) {
                    entitlementDao.insert(e);
                    res.grantsWritten++;
                }
                res.usersUpdated++;
            }

            // leavers
            for (InternalUser u : userDao.findActive()) {
                if (!seen.contains(u.getUserId())) {
                    entitlementDao.revokeAll(u.getUserId());
                }
            }
            res.usersRevoked = userDao.terminateMissing(seen);
            res.status = "OK";
        } catch (Exception ex) {
            LOG.error("recalc failed", ex);
            res.status = "FAILED";
        } finally {
            audit.setExtractFile(res.extractFile);
            audit.setRowsRead(res.rowsRead);
            audit.setRowsRejected(res.rowsRejected);
            audit.setUsersRevoked(res.usersRevoked);
            audit.setStatus(res.status);
            batchAuditDao.finish(audit);
            LOG.info(JOB_NAME + " " + res);
        }
        return res;
    }

}
