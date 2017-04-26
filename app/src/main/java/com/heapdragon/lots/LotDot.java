package com.heapdragon.lots;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LotDot extends RelativeLayout {

    private class SizableFAB extends FloatingActionButton {
        private float multiplier;
        public SizableFAB(Context context, Float multiplier) {
            super(context);
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

    private SizableFAB outterDot;
    private SizableFAB innerDot;
    private TextView number;


    public LotDot(Context context) {
        super(context);
        innerDot = new SizableFAB(context,1f);
        outterDot = new SizableFAB(context,1.5f);
        number = new TextView(context);
        number.setTextColor(getResources().getColor(R.color.colorWhite));
        number.setText("0");
        number.setTextSize(20);

        setOutterDotColor(R.color.colorAmber);
        setInnerDotColor(R.color.colorBlueGrey);

        RelativeLayout.LayoutParams outterParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams innerParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);


        innerParams.addRule(CENTER_IN_PARENT);
        outterParams.addRule(CENTER_IN_PARENT);
        numberParams.addRule(CENTER_IN_PARENT);


        outterDot.setElevation(0);
        innerDot.setElevation(0);
        number.setElevation(20);

        this.addView(outterDot,outterParams);
        this.addView(innerDot,innerParams);
        this.addView(number,numberParams);

        this.setNumber(7777);

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
