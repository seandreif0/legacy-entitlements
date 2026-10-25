package com.meridian.ent.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.meridian.ent.model.HrExtractRow;

public class HrExtractReaderTest {

    @Test
    public void readsRowsAndRejectsShortLines() throws Exception {
        HrExtractReader reader = new HrExtractReader();
        List<HrExtractRow> rows = reader.read(new File("target/test-classes/extracts/HR_USERS_20240311.csv"));

        assertEquals(5, rows.size());
        assertEquals(1, reader.getRejected());
        assertEquals("u10001", rows.get(0).empId);
        assertTrue(rows.get(1).isActive());
        assertFalse(rows.get(4).isActive());
    }
}
