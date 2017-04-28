package com.heapdragon.lots;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

/**
 * Created by Francesco on 2017-04-26.
 */

public class ResizableFAB extends FloatingActionButton {


    private Float multiplier;

    public ResizableFAB(Context context) {
        super(context);
        multiplier = 1.5f;
    }

    public ResizableFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        multiplier = 1.5f;

    }
    public void setDotColor(int color){
        this.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(),color)));
        multiplier = 1.5f;

    }

    public void setMultiplier(Float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension((int) (width * multiplier), (int) (height * multiplier));
    }
}
