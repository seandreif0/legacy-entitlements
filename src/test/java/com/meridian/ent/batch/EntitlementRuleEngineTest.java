package com.meridian.ent.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.meridian.ent.model.Entitlement;
import com.meridian.ent.model.HrExtractRow;

public class EntitlementRuleEngineTest {

    private HrExtractRow row(String dept, String cc, int grade) {
        HrExtractRow r = new HrExtractRow();
        r.empId = "u1";
        r.dept = dept;
        r.costCenter = cc;
        r.grade = grade;
        r.empStatus = "ACTIVE";
        return r;
    }

    @Test
    public void seniorResearchGetsCreditRaw() {
        List<Entitlement> l = new EntitlementRuleEngine().derive(row("RSCH", "CC410", 7));
        assertEquals(4, l.size());
        assertTrue(l.toString().contains("CREDIT_RAW"));
    }

    @Test
    public void juniorIndexGetsReadOnPrelim() {
        List<Entitlement> l = new EntitlementRuleEngine().derive(row("IDX", "CC220", 4));
        assertTrue(l.toString().contains("u1:INDEX_PRELIM:READ"));
    }
}
