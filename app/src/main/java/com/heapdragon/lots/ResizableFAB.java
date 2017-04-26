package com.heapdragon.lots;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by Francesco on 2017-04-26.
 */

public class ResizableFAB extends FloatingActionButton {

    private float multiplier;

    public ResizableFAB(Context context,Float multiplier) {
            super(context);
            this.multiplier = multiplier;
    }

    public ResizableFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            setMeasuredDimension((int) (width * multiplier), (int) (height * multiplier));
    }

}
