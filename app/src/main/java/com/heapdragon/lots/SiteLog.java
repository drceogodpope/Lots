package com.heapdragon.lots;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Comparator;

public class SiteLog implements Comparator<SiteLog>{

    private final String NOT_READY = "Not Ready";
    private final String READY = "Ready";
    private final String ISSUE = "Issue";
    private final String RECEIVED = "Received";

    static final long PRIMARY = 0L;
    static final long SECONDARY = 1L;


    private DateTime dateTime;
    private int lotNumber;
    private String user;
    private int status;
    private String title;
    private String logKey;
    private String siteKey;
    private long priority;

    public SiteLog(DateTime dateTime,long lotNumber,int status,String logKey,String siteKey,long priority) {
        this.dateTime = dateTime;
        this.lotNumber = (int)lotNumber;
        this.status = status;
        user = "User Name"; // change later
        title = "Lot " + String.valueOf(lotNumber) + " - " + getStatusString(status,priority);
        this.logKey = logKey;
        this.siteKey = siteKey;
        this.priority = priority;
    }

    private String getStatusString(int status,long priority) {
        if(priority==PRIMARY){
            switch (status){
                case 0:
                    return NOT_READY;
                case 1:
                    return READY;
                case 2:
                    return ISSUE;
                default:
                    return RECEIVED;
            }
        }
        switch (status){

            case 1:
                return "Material Ordered";
            case 2:
                return "Arch in Shipping";
            case 3:
                return "Arch in Production";
            default:
                return "Arch Required";
        }

    }

    DateTime getDateTime() {
        return dateTime;
    }

    public int getLotNumber() {
        return lotNumber;
    }

    String getUser() {
        return user;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    String getTimeStamp(){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        return dateTime.toString(dtf);
    }

    @Override
    public int compare(SiteLog siteLog, SiteLog t1) {
        return siteLog.getDateTime().compareTo(t1.getDateTime());
    }

    String getLogKey() {
        return logKey;
    }

    public long getPriority() {
        return priority;
    }

    String getSiteKey() {
        return siteKey;
    }
}
