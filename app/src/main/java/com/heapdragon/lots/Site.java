package com.heapdragon.lots;

public class Site{
    private final static String TAG = "Site";

    private String id;
    private String name;
    private int n;
    private int m;
    private int numberOfLots;
    private int incompleteLots;
    private int receivedLots;
    private int readyLots;
    private int issue_lots;
    private int siteColor;

    public Site(String name,int n,int m){
        this.name = name;
        this.numberOfLots = (m-n)+1;
        this.incompleteLots = numberOfLots;
        this.receivedLots = 0;
        this.readyLots = 0;
        this.issue_lots = 0;
        this.n = n;
        this.m = m;
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

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public String getName() {return name;}
    public void setId(String id) {this.id = id;}
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    int getNumberOfLots() {
        return numberOfLots;
    }
    int getIncompleteLots() {
        return incompleteLots;
    }
    int getReceivedLots() {
        return receivedLots;
    }
    int getReadyLots() {
        return readyLots;
    }
    int getIssue_lots() {
        return issue_lots;
    }
    int getSiteColor() {return siteColor;}
}