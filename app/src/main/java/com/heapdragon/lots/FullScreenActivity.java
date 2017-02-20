package com.heapdragon.lots;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import java.io.FileInputStream;

public class FullScreenActivity extends Activity {

    private static final String TAG = "FullScreenActivity";
    private TouchImageView touchImageView;
    private Bitmap bmp;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_full_screen);
        touchImageView = (TouchImageView) findViewById(R.id.fullscreen_touchImageView);
        pb = (ProgressBar) findViewById(R.id.full_screen_activity_progressBar);
        //SET VIEW TO FULL SCREEN
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        //SET VIEW TO FULL SCREEN
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


        try {
            Log.d(TAG, "TRYING downloadBitMapThread()");

            Log.d(TAG, "creating input stream");
            FileInputStream is = getApplicationContext().openFileInput("bitmap.png");

            Log.d(TAG, "slurping bitmap");
            bmp = BitmapFactory.decodeStream(is);

            Log.d(TAG, "setting image bitmap");
            touchImageView.setImageBitmap(bmp);
            pb.setVisibility(View.GONE);
            Log.d(TAG, "closing input stream");
            is.close();

        } catch (Exception e) {
            Log.d(TAG, "CATCHING downloadBitMapThread()");
            e.printStackTrace();
            onBackPressed();
        }
    }





}
