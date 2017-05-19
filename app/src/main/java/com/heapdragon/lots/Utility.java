package com.heapdragon.lots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.ByteArrayOutputStream;

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



    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 100);
    }

    public static int calculateNoOfColumnsFromView(Context context,View view) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int viewWidth = view.getWidth();
        return (int) (dpWidth / 100);
        }


    public static byte[] compressBitmap(Bitmap bm){
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
         return stream.toByteArray();
    }

    public static Bitmap uncompressBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
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
