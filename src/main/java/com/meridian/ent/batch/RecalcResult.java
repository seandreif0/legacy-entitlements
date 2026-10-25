package com.meridian.ent.batch;

public class RecalcResult {

    public String extractFile;
    public int rowsRead;
    public int rowsRejected;
    public int usersUpdated;
    public int usersRevoked;
    public int grantsWritten;
    public String status;

    @Override
    public String toString() {
        return "status=" + status + " file=" + extractFile + " read=" + rowsRead + " rejected=" + rowsRejected
            + " updated=" + usersUpdated + " revoked=" + usersRevoked + " grants=" + grantsWritten;
    }
}
