package com.heapdragon.lots;

/**
 * Created by Francesco on 2016-12-21.
 */

public class Lot {

    public static final int NOT_READY = 0;
    public static final int READY = 1;
    public static final int ISSUE = 2;
    public static final int RECEIVED = 3;

    private long number;
    private long status;  // 0-not ready,1-ready,2-issue,3-received

    public Lot(long number, long status){
        this.number = number;
        this.status = status;
    }

    public long getNumber() {
        return number;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        if(status>=0 && status<4){
            this.status = status;
        }
    }
}
