package com.meridian.ent.batch;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.meridian.ent.util.DateUtil;

/**
 * Finds today's HR extract. Strict: the job aborts if today's file is absent.
 */
public class ExtractLocator {

    private static final Logger LOG = Logger.getLogger(ExtractLocator.class);

    private String extractDir;
    private String prefix = "HR_USERS_";
    private int maxAgeDays = 3;

    public File locate(Date runDate) {
        for (int age = 0; age <= maxAgeDays; age++) {
            String name = prefix + DateUtil.feedStamp(DateUtil.minusDays(runDate, age)) + ".csv";
            File f = new File(extractDir, name);
            if (f.exists() && f.length() > 0) {
                if (age > 0) {
                    LOG.debug("using " + name);
                }
                return f;
            }
        }
        return null;
    }

    public void setExtractDir(String extractDir) { this.extractDir = extractDir; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public void setMaxAgeDays(int maxAgeDays) { this.maxAgeDays = maxAgeDays; }
}
