package com.heapdragon.lots;

public class Site{
    private final static String TAG = "Site";
    public static int totalSites = 0;
    private String id;
    private String name;
    private int numberOfLots;
    private int incompleteLots;
    private int receivedLots;
    private int readyLots;
    private int issue_lots;
    private int siteColor;

    public Site(String name,int numberOfLots){
        this.name = name;
        this.numberOfLots = numberOfLots;
        this.incompleteLots = numberOfLots;
        this.receivedLots = 0;
        this.readyLots = 0;
        this.issue_lots = 0;
        this.siteColor = totalSites%4;
        totalSites++;
    }

    public Site(String name, int numberOfLots, int incompleteLots, int issueLots, int readyLots, int receivedLots, int siteColor,String id) {
        this.name = name;
        this.numberOfLots = numberOfLots;
        this.incompleteLots = incompleteLots;
        this.issue_lots = issueLots;
        this.readyLots = readyLots;
        this.receivedLots = receivedLots;
        this.siteColor = siteColor;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfLots() {
        return numberOfLots;
    }

    public void setNumberOfLots(int numberOfLots) {
        this.numberOfLots = numberOfLots;
    }

    public int getIncompleteLots() {
        return incompleteLots;
    }

    public void setIncompleteLots(int incompleteLots) {
        this.incompleteLots = incompleteLots;
    }

    public int getReceivedLots() {
        return receivedLots;
    }

    public void setReceivedLots(int receivedLots) {
        this.receivedLots = receivedLots;
    }

    public int getReadyLots() {
        return readyLots;
    }

    public void setReadyLots(int readyLots) {
        this.readyLots = readyLots;
    }

    public int getIssue_lots() {
        return issue_lots;
    }

    public void setIssue_lots(int issue_lots) {
        this.issue_lots = issue_lots;
    }

    public int getSiteColor() {return siteColor;}
}