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

    //ARCH STATUSES
    static final long WORK_ORDER_REQUIRED = 0;
    static final long ARCH_IN_PRODUCTION_1 = 1;
    static final long ARCH_IN_SHIPPING_1 = 2;

    private long number;
    private long primaryStatus;
    private long secondaryStatus;

    private Boolean materialOrdered;
    private Boolean archLot;
    private Boolean archOrdered;
    private long archStatus;

//    public Lot(long number, long primaryStatus, long secondaryStatus){
//        this.number = number;
//        this.primaryStatus = primaryStatus;
//        this.secondaryStatus = secondaryStatus;
//    }

    public Lot(long number,Boolean materialOrdered,Boolean archLot, Boolean archOrdered, long archStatus){
        this.number = number;
        this.materialOrdered = materialOrdered;
        this.archLot = archLot;
        this.archStatus = archStatus;
        this.archOrdered = archOrdered;
    }


    long getNumber() {
        return number;
    }
    long getPrimaryStatus() {
        return primaryStatus;
    }
    long getSecondaryStatus() {
        return secondaryStatus;
    }

    public Boolean getArchLot() {
        return archLot;
    }

    public Boolean getMaterialOrdered() {
        return materialOrdered;
    }

    public Boolean getArchOrdered() {
        return archOrdered;
    }

    public long getArchStatus() {
        return archStatus;
    }
}
