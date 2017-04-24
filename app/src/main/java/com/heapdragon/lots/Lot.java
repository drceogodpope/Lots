package com.heapdragon.lots;

/**
 * Created by Francesco on 2016-12-21.
 */

public class Lot {

    // PRIMARY STATUSES
    public static final int NOT_READY = 0;
    public static final int READY = 1;
    public static final int ISSUE = 2;
    public static final int RECEIVED = 3;

    // SECONDARY STATUSES
    public static final int NOTHING = 0;
    public static final int MATERIAL_ORDERED = 1;
    public static final int ARCH_IN_SHIPPING = 2;
    public static final int ARCH_IN_PRODUCTION = 3;
    public static final int ARCH_REQUIRED = 4;


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
