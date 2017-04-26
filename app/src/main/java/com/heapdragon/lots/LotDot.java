package com.heapdragon.lots;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LotDot extends RelativeLayout {



    private ResizableFAB outterDot;
    private ResizableFAB innerDot;
    private TextView number;


    public LotDot(Context context,TextView number,FloatingActionButton innerDot,FloatingActionButton outerDot) {
        super(context);

    }

    public void setOutterDotColor(int color){
        outterDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(),color)));
    }
    public void setInnerDotColor(int color){
        innerDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(),color)));
    }

    public void setNumber(int number){
        this.number.setText(String.valueOf(number));
    }


}
