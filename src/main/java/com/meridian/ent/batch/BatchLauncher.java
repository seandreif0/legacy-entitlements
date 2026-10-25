package com.meridian.ent.batch;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import com.meridian.ent.config.EntitlementDataConfig;

/**
 * Manual rerun for ops: java -cp ... com.meridian.ent.batch.BatchLauncher
 * Pass -Dent.extract.dir=... to point at another drop folder.
 */
public class BatchLauncher {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        // system properties take precedence over application.properties
        ctx.getEnvironment().getPropertySources().addLast(new ResourcePropertySource("classpath:application.properties"));
        ctx.register(EntitlementDataConfig.class);
        ctx.scan("com.meridian.ent.batch", "com.meridian.ent.dao");
        ctx.refresh();
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
