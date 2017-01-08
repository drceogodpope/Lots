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

    private DateTime dateTime;
    private int lotNumber;
    private String user;
    private int status;
    private String title;

    public SiteLog(DateTime dateTime,long lotNumber,int status) {
        this.dateTime = dateTime;
        this.lotNumber = (int)lotNumber;
        this.status = status;
        user = "User Name"; // change later
        title = "Lot - " + String.valueOf(lotNumber) + " " + getStatusString(status);
    }

    private String getStatusString(int status) {
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

    public DateTime getDateTime() {
        return dateTime;
    }

    public int getLotNumber() {
        return lotNumber;
    }

    public String getUser() {
        return user;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeStamp(){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        return dateTime.toString(dtf);
    }

    @Override
    public int compare(SiteLog siteLog, SiteLog t1) {
        return siteLog.getDateTime().compareTo(t1.getDateTime());
    }

}
