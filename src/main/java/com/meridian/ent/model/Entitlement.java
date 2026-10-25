package com.meridian.ent.model;

import java.util.Date;

public class Entitlement {

    public static final String SRC_BATCH = "BATCH";
    public static final String SRC_MANUAL = "MANUAL";

    private long entId;
    private String userId;
    private String datasetCode;
    private String accessLevel;
    private String source = SRC_BATCH;
    private Date grantedDate;

    public Entitlement() {
    }

    public Entitlement(String userId, String datasetCode, String accessLevel) {
        this.userId = userId;
        this.datasetCode = datasetCode;
        this.accessLevel = accessLevel;
    }

    public long getEntId() { return entId; }
    public void setEntId(long entId) { this.entId = entId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDatasetCode() { return datasetCode; }
    public void setDatasetCode(String datasetCode) { this.datasetCode = datasetCode; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Date getGrantedDate() { return grantedDate; }
    public void setGrantedDate(Date grantedDate) { this.grantedDate = grantedDate; }

    @Override
    public String toString() {
        return userId + ":" + datasetCode + ":" + accessLevel;
    }
}
