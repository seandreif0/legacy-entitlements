package com.meridian.ent.model;

/**
 * One line of the HRIS feed.
 * EMP_ID,FULL_NAME,DEPT,COST_CTR,GRADE,EMP_STATUS
 */
public class HrExtractRow {

    public String empId;
    public String fullName;
    public String dept;
    public String costCenter;
    public int grade;
    public String empStatus;

    public boolean isActive() {
        // HRIS sends A, ACT or ACTIVE depending on region
        return empStatus != null && empStatus.toUpperCase().startsWith("A");
    }
}
