package com.heapdragon.lots;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

class LotDot extends View{

    public LotDot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LotDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LotDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    public void setColor(int color){
        this.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(),color)));
    }

    public ColorStateList getColorStateListe(){
        return this.getBackgroundTintList();
    }


}
