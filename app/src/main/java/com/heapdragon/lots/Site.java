package com.heapdragon.lots;

/**
 * Created by Francesco on 2016-12-21.
 */

public class Site{
    private String name;
    private String id;

    private int totalLots;
    private int incompleteLots;
    private int receivedLots;
    private int readyLots;
    private int issue_lots;

    public Site(String name, String id, int totalLots, int incompleteLots, int receivedLots, int readyLots, int issue_lots) {
        this.name = name;
        this.id = id;
        this.totalLots = totalLots;
        this.incompleteLots = incompleteLots;
        this.receivedLots = receivedLots;
        this.readyLots = readyLots;
        this.issue_lots = issue_lots;
    }

    public Site(String name, int totalLots){
        this.name = name;
        this.totalLots = totalLots;
        incompleteLots = totalLots;
        receivedLots = 0;
        readyLots = 0;
        issue_lots = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(int totalLots) {
        this.totalLots = totalLots;
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
}