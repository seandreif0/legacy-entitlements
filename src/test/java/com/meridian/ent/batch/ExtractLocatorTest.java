package com.meridian.ent.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

public class ExtractLocatorTest {

    private ExtractLocator locator(int maxAge) {
        ExtractLocator l = new ExtractLocator();
        l.setExtractDir("target/test-classes/extracts");
        l.setMaxAgeDays(maxAge);
        return l;
    }

    @Test
    public void findsSameDayFile() throws Exception {
        File f = locator(3).locate(new SimpleDateFormat("yyyyMMdd").parse("20240311"));
        assertEquals("HR_USERS_20240311.csv", f.getName());
    }

    @Test
    public void usesPreviousFileWhenTodaysIsMissing() throws Exception {
        File f = locator(3).locate(new SimpleDateFormat("yyyyMMdd").parse("20240313"));
        assertEquals("HR_USERS_20240311.csv", f.getName());
    }

    @Test
    public void returnsNullWhenNothingRecent() throws Exception {
        assertNull(locator(1).locate(new SimpleDateFormat("yyyyMMdd").parse("20240320")));
    }
}
