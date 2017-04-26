package com.heapdragon.lots;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

class LotDot{

    private FloatingActionButton outterDot;
    private ResizableFAB innerDot;
    private TextView number;


    public LotDot(TextView number,ResizableFAB innerDot,FloatingActionButton outerDot) {
        this.innerDot = innerDot;
        this.outterDot = outerDot;
        this.number = number;
    }

    public void setOutterDotColor(int color){
        outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.innerDot.getContext(),color)));
    }
    public void setInnerDotColor(int color){
        innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.innerDot.getContext(),color)));
    }

    public void setNumber(int number){
        this.number.setText(String.valueOf(number));
    }


}
