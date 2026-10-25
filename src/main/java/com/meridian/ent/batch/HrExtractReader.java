package com.meridian.ent.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.meridian.ent.model.HrExtractRow;

public class HrExtractReader {

    private static final Logger LOG = Logger.getLogger(HrExtractReader.class);

    private int rejected;

    public List<HrExtractRow> read(File file) throws IOException {
        List<HrExtractRow> rows = new ArrayList<HrExtractRow>();
        rejected = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < 6) {
                    rejected++;
                    continue;
                }
                HrExtractRow r = new HrExtractRow();
                r.empId = p[0].trim().toLowerCase();
                r.fullName = p[1].trim();
                r.dept = p[2].trim();
                r.costCenter = p[3].trim();
                try {
                    r.grade = Integer.parseInt(p[4].trim());
                } catch (NumberFormatException nfe) {
                    r.grade = 0;
                }
                r.empStatus = p[5].trim();
                rows.add(r);
            }
        } finally {
            br.close();
        }
        if (rejected > 0) {
            LOG.warn(rejected + " rows rejected from " + file.getName());
        }
        return rows;
    }

    public int getRejected() {
        return rejected;
    }
}
