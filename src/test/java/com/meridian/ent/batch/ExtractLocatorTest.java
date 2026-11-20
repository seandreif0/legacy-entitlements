package com.meridian.ent.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ExtractLocatorTest {

    private ExtractLocator locator(int maxAge) {
        ExtractLocator l = new ExtractLocator();
        l.setExtractDir("target/test-classes/extracts");
        l.setMaxAgeDays(maxAge);
        return l;
    }

    @Test
    public void findsSameDayFile() {
        File f = locator(3).locate(LocalDate.of(2024, 3, 11));
        assertEquals("HR_USERS_20240311.csv", f.getName());
    }

    @Test
    public void usesPreviousFileWhenTodaysIsMissing() {
        File f = locator(3).locate(LocalDate.of(2024, 3, 13));
        assertEquals("HR_USERS_20240311.csv", f.getName());
    }

    @Test
    public void returnsNullWhenNothingRecent() {
        assertNull(locator(1).locate(LocalDate.of(2024, 3, 20)));
    }
}
