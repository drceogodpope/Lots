package com.heapdragon.lots;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;

/**
 * Created by Francesco on 2017-02-14.
 */

public class FullScreenActivity extends Activity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_full_screen);
        touchImageView = (TouchImageView) findViewById(R.id.fullscreen_touchImageView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = getIntent().getByteArrayExtra("bitmap");
                touchImageView.setImageBitmap(Utility.uncompressBitmap(bytes));
            }
        }).start();

        touchImageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                onBackPressed();
                return true;
            }
            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return false;
            }
            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }


}
