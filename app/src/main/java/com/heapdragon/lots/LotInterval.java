package com.heapdragon.lots;

class LotInterval{
    private long n;
    private long m;

    LotInterval(long n,long m){
        this.n = n;
        this.m = m;
    }
    long getM() {
        return m;
    }
    long getN() {
        return n;
    }

    public String toLogString(){
        String nValue = String.valueOf(n);
        String mValue = String.valueOf(m);
        return "(" + nValue + "," +mValue+")";
    }

    public String toString(){
        String nValue = String.valueOf(n);
        String mValue = String.valueOf(m);
        return  nValue + ":" +mValue;
    }
}
