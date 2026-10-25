package com.meridian.ent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.meridian.ent.model.Entitlement;

/**
 * Entitlement persistence. All writes are transactional.
 */
public class OracleEntitlementDao implements EntitlementDao {

    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Entitlement> MAPPER = new RowMapper<Entitlement>() {
        public Entitlement mapRow(ResultSet rs, int rowNum) throws SQLException {
            Entitlement e = new Entitlement();
            e.setEntId(rs.getLong("ENT_ID"));
            e.setUserId(rs.getString("USER_ID"));
            e.setDatasetCode(rs.getString("DATASET_CODE"));
            e.setAccessLevel(rs.getString("ACCESS_LVL"));
            e.setSource(rs.getString("SOURCE"));
            e.setGrantedDate(rs.getTimestamp("GRANTED_DT"));
            return e;
        }
    };

    public List<Entitlement> findByUser(String userId) {
        return jdbcTemplate.query(
            "SELECT ENT_ID, USER_ID, DATASET_CODE, ACCESS_LVL, NVL(SOURCE, 'BATCH') SOURCE, GRANTED_DT "
                + "FROM USER_ENTITLEMENT WHERE USER_ID = ? ORDER BY DATASET_CODE",
            MAPPER, userId);
    }

    public boolean hasAccess(String userId, String datasetCode) {
        Integer n = jdbcTemplate.queryForObject(
            "SELECT COUNT(1) FROM USER_ENTITLEMENT E JOIN INTERNAL_USER U ON U.USER_ID = E.USER_ID "
                + "WHERE E.USER_ID = ? AND E.DATASET_CODE = ? AND U.STATUS = 'ACTIVE'",
            Integer.class, userId, datasetCode);
        return n != null && n > 0;
    }

    public void insert(Entitlement e) {
        jdbcTemplate.update(
            "INSERT INTO USER_ENTITLEMENT (ENT_ID, USER_ID, DATASET_CODE, ACCESS_LVL, SOURCE, GRANTED_DT) "
                + "VALUES (ENT_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)",
            e.getUserId(), e.getDatasetCode(), e.getAccessLevel(), e.getSource());
    }

    public int deleteBatchGrants(String userId) {
        return jdbcTemplate.update(
            "DELETE FROM USER_ENTITLEMENT WHERE USER_ID = ? AND NVL(SOURCE, 'BATCH') = 'BATCH'", userId);
    }

    public int revokeAll(String userId) {
        // manual grants are kept for audit
        return jdbcTemplate.update("DELETE FROM USER_ENTITLEMENT WHERE USER_ID = ?", userId);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
