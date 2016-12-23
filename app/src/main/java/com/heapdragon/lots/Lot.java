package com.heapdragon.lots;

/**
 * Created by Francesco on 2016-12-21.
 */

public class Lot {

    private int number;
    private int status;  // 0-not ready,1-ready,2-issue,3-received

    public Lot(int number){
        this.number = number;
        status = 0;
    }

    public int getNumber() {
        return number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if(status>=0 && status<4){
            this.status = status;
        }
    }
}
