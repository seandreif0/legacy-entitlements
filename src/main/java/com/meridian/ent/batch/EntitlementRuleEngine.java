package com.meridian.ent.batch;

import java.util.ArrayList;
import java.util.List;

import com.meridian.ent.model.Entitlement;
import com.meridian.ent.model.HrExtractRow;

/**
 * Derives dataset entitlements from department / grade.
 * Rules agreed with Data Governance 2013, see ENT-DG-004.
 */
public class EntitlementRuleEngine {

    public List<Entitlement> derive(HrExtractRow row) {
        List<Entitlement> out = new ArrayList<Entitlement>();
        String u = row.empId;

        // everyone
        out.add(new Entitlement(u, "REF_DATA", "READ"));

        if ("RSCH".equals(row.dept)) {
            out.add(new Entitlement(u, "PRICING_EOD", "READ"));
            out.add(new Entitlement(u, "ESG_SCORES", "READ"));
            if (row.grade >= 6) {
                out.add(new Entitlement(u, "CREDIT_RAW", "READ"));
            }
        } else if ("IDX".equals(row.dept)) {
            out.add(new Entitlement(u, "PRICING_EOD", "READ"));
            out.add(new Entitlement(u, "INDEX_PRELIM", row.grade >= 5 ? "WRITE" : "READ"));
        } else if ("OPS".equals(row.dept)) {
            out.add(new Entitlement(u, "PRICING_EOD", "READ"));
        } else if ("ESG".equals(row.dept)) {
            out.add(new Entitlement(u, "ESG_SCORES", "WRITE"));
        }

        // temp: CC900 contractors get read on prelim for the 2015 rebalance project
        if ("CC900".equals(row.costCenter)) {
            out.add(new Entitlement(u, "INDEX_PRELIM", "READ"));
        }
        return out;
    }
}
