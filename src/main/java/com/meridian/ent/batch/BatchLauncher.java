package com.meridian.ent.batch;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.meridian.ent.config.EntitlementDataConfig;

/**
 * Manual rerun for ops: java -cp ... com.meridian.ent.batch.BatchLauncher
 * Pass -Dent.extract.dir=... to point at another drop folder.
 */
public class BatchLauncher {

    @Configuration
    @Import(EntitlementDataConfig.class)
    @ImportAutoConfiguration({DataSourceAutoConfiguration.class, SqlInitializationAutoConfiguration.class})
    @ComponentScan({"com.meridian.ent.batch", "com.meridian.ent.dao"})
    static class LauncherConfig {
    }

    public static void main(String[] args) throws Exception {
        // system properties take precedence over application.properties
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(LauncherConfig.class)
            .web(WebApplicationType.NONE)
            .run(args);
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
