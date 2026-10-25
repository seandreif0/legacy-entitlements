package com.meridian.ent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.meridian.ent.model.InternalUser;

public class OracleUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_COLS =
        "SELECT USER_ID, FULL_NAME, DEPT_CODE, COST_CENTER, NVL(GRADE, 0) GRADE, STATUS, LAST_SEEN_DT FROM INTERNAL_USER ";

    private static final RowMapper<InternalUser> MAPPER = new RowMapper<InternalUser>() {
        public InternalUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            InternalUser u = new InternalUser();
            u.setUserId(rs.getString("USER_ID"));
            u.setFullName(rs.getString("FULL_NAME"));
            u.setDeptCode(rs.getString("DEPT_CODE"));
            u.setCostCenter(rs.getString("COST_CENTER"));
            u.setGrade(rs.getInt("GRADE"));
            u.setStatus(rs.getString("STATUS"));
            u.setLastSeen(rs.getTimestamp("LAST_SEEN_DT"));
            return u;
        }
    };

    public InternalUser findById(String userId) {
        List<InternalUser> l = jdbcTemplate.query(SELECT_COLS + "WHERE USER_ID = ?", MAPPER, userId);
        return l.isEmpty() ? null : l.get(0);
    }

    public List<InternalUser> findActive() {
        return jdbcTemplate.query(SELECT_COLS + "WHERE STATUS = 'ACTIVE' ORDER BY USER_ID", MAPPER);
    }

    public void upsert(InternalUser u) {
        int n = jdbcTemplate.update(
            "UPDATE INTERNAL_USER SET FULL_NAME = ?, DEPT_CODE = ?, COST_CENTER = ?, GRADE = ?, STATUS = 'ACTIVE', LAST_SEEN_DT = SYSDATE WHERE USER_ID = ?",
            u.getFullName(), u.getDeptCode(), u.getCostCenter(), u.getGrade(), u.getUserId());
        if (n == 0) {
            jdbcTemplate.update(
                "INSERT INTO INTERNAL_USER (USER_ID, FULL_NAME, DEPT_CODE, COST_CENTER, GRADE, STATUS, LAST_SEEN_DT) VALUES (?, ?, ?, ?, ?, 'ACTIVE', SYSDATE)",
                u.getUserId(), u.getFullName(), u.getDeptCode(), u.getCostCenter(), u.getGrade());
        }
    }

    public int terminateMissing(Collection<String> seenUserIds) {
        // Oracle IN-list limit is 1000; feed is ~600 so fine for now
        int count = 0;
        for (InternalUser u : findActive()) {
            if (!seenUserIds.contains(u.getUserId())) {
                jdbcTemplate.update("UPDATE INTERNAL_USER SET STATUS = 'TERMINATED' WHERE USER_ID = ?", u.getUserId());
                count++;
            }
        }
        return count;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
