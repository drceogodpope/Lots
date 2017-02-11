package com.heapdragon.lots;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

class Utility {
    public static int calculateNoOfColumns(Context context,int imageSize) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / imageSize);
    }

    static String capitilizeFirst(String string){
            StringBuilder titleCase = new StringBuilder();
            boolean nextTitleCase = true;
            for (char c : string.toCharArray()) {
                if (Character.isSpaceChar(c)) {
                    nextTitleCase = true;
                } else if (nextTitleCase) {
                    c = Character.toTitleCase(c);
                    nextTitleCase = false;
                }
                titleCase.append(c);
            }
            return titleCase.toString();
    }

    /**
     * Returns darker version of specified <code>color</code>.
     */
     static int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

}
