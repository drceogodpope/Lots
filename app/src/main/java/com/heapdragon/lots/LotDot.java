package com.heapdragon.lots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.*;

class LotDot extends View{

    private ValueAnimator colorAnimation;

    public LotDot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ArrayList<Integer> flashyColors = new ArrayList<>();

        int purple = ContextCompat.getColor(context,R.color.colorPurple1);
        int yellow = ContextCompat.getColor(context,R.color.colorAmber);
        int blue = ContextCompat.getColor(context,R.color.colorBlue3);
        int green = ContextCompat.getColor(context,R.color.colorGreen);

        flashyColors.add(purple);
        flashyColors.add(yellow);
        flashyColors.add(blue);
        flashyColors.add(green);
        colorAnimation = ColorAnimFactory.flashingView(flashyColors,this,2000);
    }

    public LotDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ArrayList<Integer> flashyColors = new ArrayList<>();

        int purple = ContextCompat.getColor(context,R.color.colorPurple1);
        int yellow = ContextCompat.getColor(context,R.color.colorAmber);
        int blue = ContextCompat.getColor(context,R.color.colorBlue3);
        int green = ContextCompat.getColor(context,R.color.colorGreen);

        flashyColors.add(purple);
        flashyColors.add(yellow);
        flashyColors.add(blue);
        flashyColors.add(green);
        colorAnimation = ColorAnimFactory.flashingView(flashyColors,this,2000);
    }

    public LotDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void setColor(int color){
        this.setBackgroundTintList(ColorStateList.valueOf(getColor(this.getContext(),color)));
    }

    public void startFlashing(){
        colorAnimation.start();
    }

    public void endFlashing(){
        colorAnimation.end();
    }



    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
    {
        super.onSizeChanged(w, w, oldw, oldh);
    }

    public ColorStateList getColorStateList(){
        return this.getBackgroundTintList();
    }


}
