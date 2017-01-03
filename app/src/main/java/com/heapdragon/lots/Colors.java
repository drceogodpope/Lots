package com.heapdragon.lots;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by Francesco on 2017-01-02.
 */

public class Colors {
    static int cyan;
    static int pink;
    static int amber;
    static int indigo;

    public Colors(Context context){
        cyan = ContextCompat.getColor(context,R.color.colorCyan);
        pink = ContextCompat.getColor(context,R.color.colorPink);
        amber = ContextCompat.getColor(context,R.color.colorAmber);
        indigo = ContextCompat.getColor(context,R.color.colorIndigo);
    }

    public static int getIndigo() {
        return indigo;
    }

    public static int getCyan() {
        return cyan;
    }

    public static int getPink() {
        return pink;
    }

    public static int getAmber() {
        return amber;
    }
}
