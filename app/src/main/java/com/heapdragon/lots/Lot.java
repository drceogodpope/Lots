package com.heapdragon.lots;

public class Lot {

    // PRIMARY STATUSES
    static final int NOT_READY = 0;
    static final int READY = 1;
    static final int ISSUE = 2;
    static final int RECEIVED = 3;

    // SECONDARY STATUSES
    public static final int NOTHING = 0;
    static final int MATERIAL_ORDERED = 1;
    static final int ARCH_IN_SHIPPING = 2;
    static final int ARCH_IN_PRODUCTION = 3;
    static final int ARCH_REQUIRED = 4;

    //STATUS LEVELS
    static final int PRIMARY = 0;
    static final int SECONDARY = 1;

    private long number;
    private long primaryStatus;
    private long secondaryStatus;

    public Lot(long number, long primaryStatus, long secondaryStatus){
        this.number = number;
        this.primaryStatus = primaryStatus;
        this.secondaryStatus = secondaryStatus;
    }

    public long getNumber() {
        return number;
    }

    public long getPrimaryStatus() {
        return primaryStatus;
    }

    public long getSecondaryStatus() {
        return secondaryStatus;
    }
}
