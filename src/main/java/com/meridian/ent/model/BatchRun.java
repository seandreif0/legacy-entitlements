package com.meridian.ent.model;

import java.util.Date;

public class BatchRun {

    private long runId;
    private String jobName;
    private String extractFile;
    private Date started;
    private Date ended;
    private int rowsRead;
    private int rowsRejected;
    private int usersRevoked;
    private String status;

    public long getRunId() { return runId; }
    public void setRunId(long runId) { this.runId = runId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getExtractFile() { return extractFile; }
    public void setExtractFile(String extractFile) { this.extractFile = extractFile; }
    public Date getStarted() { return started; }
    public void setStarted(Date started) { this.started = started; }
    public Date getEnded() { return ended; }
    public void setEnded(Date ended) { this.ended = ended; }
    public int getRowsRead() { return rowsRead; }
    public void setRowsRead(int rowsRead) { this.rowsRead = rowsRead; }
    public int getRowsRejected() { return rowsRejected; }
    public void setRowsRejected(int rowsRejected) { this.rowsRejected = rowsRejected; }
    public int getUsersRevoked() { return usersRevoked; }
    public void setUsersRevoked(int usersRevoked) { this.usersRevoked = usersRevoked; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
