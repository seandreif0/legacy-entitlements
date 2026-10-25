package com.meridian.ent.batch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Manual rerun for ops: java -cp ... com.meridian.ent.batch.BatchLauncher
 * Pass -Dent.extract.dir=... to point at another drop folder.
 */
public class BatchLauncher {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
            "entitlements-dao.xml", "entitlements-batch.xml", "launcher-props.xml");
        try {
            NightlyEntitlementRecalcJob job = ctx.getBean(NightlyEntitlementRecalcJob.class);
            RecalcResult r = job.recalc(new java.util.Date());
            System.out.println(r);
            System.exit("OK".equals(r.status) ? 0 : 2);
        } finally {
            ctx.close();
        }
    }
}
