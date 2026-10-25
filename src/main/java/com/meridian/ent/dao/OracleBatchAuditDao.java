package com.meridian.ent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.meridian.ent.model.BatchRun;

public class OracleBatchAuditDao implements BatchAuditDao {

    private JdbcTemplate jdbcTemplate;

    public long start(String jobName) {
        Long id = jdbcTemplate.queryForObject("SELECT BATCH_RUN_SEQ.NEXTVAL FROM DUAL", Long.class);
        jdbcTemplate.update(
            "INSERT INTO BATCH_RUN (RUN_ID, JOB_NAME, STARTED_DT, STATUS) VALUES (?, ?, SYSDATE, 'RUNNING')", id, jobName);
        return id;
    }

    public void finish(BatchRun r) {
        jdbcTemplate.update(
            "UPDATE BATCH_RUN SET EXTRACT_FILE = ?, ENDED_DT = SYSDATE, ROWS_READ = ?, ROWS_REJECTED = ?, USERS_REVOKED = ?, STATUS = ? WHERE RUN_ID = ?",
            r.getExtractFile(), r.getRowsRead(), r.getRowsRejected(), r.getUsersRevoked(), r.getStatus(), r.getRunId());
    }

    public BatchRun findLatest(String jobName) {
        List<BatchRun> l = jdbcTemplate.query(
            "SELECT * FROM BATCH_RUN WHERE JOB_NAME = ? ORDER BY RUN_ID DESC",
            new RowMapper<BatchRun>() {
                public BatchRun mapRow(ResultSet rs, int i) throws SQLException {
                    BatchRun r = new BatchRun();
                    r.setRunId(rs.getLong("RUN_ID"));
                    r.setJobName(rs.getString("JOB_NAME"));
                    r.setExtractFile(rs.getString("EXTRACT_FILE"));
                    r.setStarted(rs.getTimestamp("STARTED_DT"));
                    r.setEnded(rs.getTimestamp("ENDED_DT"));
                    r.setRowsRead(rs.getInt("ROWS_READ"));
                    r.setRowsRejected(rs.getInt("ROWS_REJECTED"));
                    r.setUsersRevoked(rs.getInt("USERS_REVOKED"));
                    r.setStatus(rs.getString("STATUS"));
                    return r;
                }
            }, jobName);
        return l.isEmpty() ? null : l.get(0);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
