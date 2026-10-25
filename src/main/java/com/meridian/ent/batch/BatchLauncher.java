package com.meridian.ent.batch;

import java.util.Date;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Manual rerun for ops: java -jar ent-svc.jar --spring.profiles.active=local,rerun (or prod,rerun).
 * Pass -Dent.extract.dir=... to point at another drop folder. Exits 0 on OK, 2 otherwise.
 */
@Component
@Profile(BatchLauncher.PROFILE)
public class BatchLauncher implements ApplicationRunner, ExitCodeGenerator {

    public static final String PROFILE = "rerun";

    private final NightlyEntitlementRecalcJob job;
    private int exitCode = 2;

    public BatchLauncher(NightlyEntitlementRecalcJob job) {
        this.job = job;
    }

    @Override
    public void run(ApplicationArguments args) {
        RecalcResult r = job.recalc(new Date());
        System.out.println(r);
        exitCode = "OK".equals(r.status) ? 0 : 2;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
