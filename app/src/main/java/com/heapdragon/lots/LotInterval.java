package com.heapdragon.lots;

/**
 * Created by Francesco on 2017-06-18.
 */

 class LotInterval {
    private int n;
    private int m;
    public LotInterval(int n,int m){
            this.n = n;
            this.m = m;
    }
    public int getM() {
        return m;
    }
    public int getN() {
        return n;
    }

    public String toString(){
        String nValue = String.valueOf(n);
        String mValue = String.valueOf(m);
        return "(" + nValue + "," +mValue+")";
    }
}
