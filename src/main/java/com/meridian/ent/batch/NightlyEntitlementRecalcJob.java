package com.meridian.ent.batch;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class NightlyEntitlementRecalcJob {

    public static final String JOB_NAME = "ENT_NIGHTLY_RECALC";

    private static final Logger LOG = LoggerFactory.getLogger(NightlyEntitlementRecalcJob.class);

    private ExtractLocator extractLocator;
    private HrExtractReader reader;
    private EntitlementRuleEngine ruleEngine;
    private UserDao userDao;
    private EntitlementDao entitlementDao;
    private BatchAuditDao batchAuditDao;

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

    public void setExtractLocator(ExtractLocator extractLocator) { this.extractLocator = extractLocator; }
    public void setReader(HrExtractReader reader) { this.reader = reader; }
    public void setRuleEngine(EntitlementRuleEngine ruleEngine) { this.ruleEngine = ruleEngine; }
    public void setUserDao(UserDao userDao) { this.userDao = userDao; }
    public void setEntitlementDao(EntitlementDao entitlementDao) { this.entitlementDao = entitlementDao; }
    public void setBatchAuditDao(BatchAuditDao batchAuditDao) { this.batchAuditDao = batchAuditDao; }
}
