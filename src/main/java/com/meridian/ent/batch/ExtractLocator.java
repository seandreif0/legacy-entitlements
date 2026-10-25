package com.meridian.ent.batch;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meridian.ent.util.DateUtil;

/**
 * Finds today's HR extract. Strict: the job aborts if today's file is absent.
 */
@Component
public class ExtractLocator {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractLocator.class);

    private String extractDir;
    private String prefix = "HR_USERS_";
    private int maxAgeDays = 3;

    public ExtractLocator() {
    }

    @Autowired
    public ExtractLocator(@Value("${ent.extract.dir}") String extractDir,
                          @Value("${ent.extract.prefix:HR_USERS_}") String prefix,
                          @Value("${ent.extract.maxAgeDays:3}") int maxAgeDays) {
        this.extractDir = extractDir;
        this.prefix = prefix;
        this.maxAgeDays = maxAgeDays;
    }

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
