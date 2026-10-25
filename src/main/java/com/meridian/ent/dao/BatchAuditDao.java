package com.meridian.ent.dao;

import com.meridian.ent.model.BatchRun;

public interface BatchAuditDao {

    long start(String jobName);

    void finish(BatchRun run);

    BatchRun findLatest(String jobName);
}
