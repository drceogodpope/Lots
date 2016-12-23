package com.heapdragon.lots;

import java.util.ArrayList;

/**
 * Created by Francesco on 2016-12-21.
 */

public class Site{
    private ArrayList<Lot> lots = new ArrayList<>();
    private int numberOfLots;
    private String name;

    public Site(String name,int numberOfLots){
        this.name = name;
        this.numberOfLots = numberOfLots;
        for(int i = 0;i<numberOfLots;i++){
            lots.add(new Lot(i+1));
        }
    }

    public int getNumberOfLots() {
        return numberOfLots;
    }

    public ArrayList<Lot> getLots() {
        return lots;
    }

    public String getName() {
        return name;
    }

}