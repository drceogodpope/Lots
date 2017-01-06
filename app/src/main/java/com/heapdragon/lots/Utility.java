package com.heapdragon.lots;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Francesco on 2017-01-05.
 */

public class Utility {
    public static int calculateNoOfColumns(Context context,int imageSize) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / imageSize);
    }
}
