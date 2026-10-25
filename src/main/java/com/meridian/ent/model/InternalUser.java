package com.meridian.ent.model;

import java.util.Date;

public class InternalUser {

    private String userId;
    private String fullName;
    private String deptCode;
    private String costCenter;
    private int grade;
    private String status;
    private Date lastSeen;

    public InternalUser() {
    }

    public InternalUser(String userId, String fullName, String deptCode, String costCenter, int grade) {
        this.userId = userId;
        this.fullName = fullName;
        this.deptCode = deptCode;
        this.costCenter = costCenter;
        this.grade = grade;
        this.status = "ACTIVE";
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getLastSeen() { return lastSeen; }
    public void setLastSeen(Date lastSeen) { this.lastSeen = lastSeen; }
}
