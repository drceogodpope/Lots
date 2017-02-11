package com.heapdragon.lots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Francesco on 2017-02-11.
 */

public class Splash extends Activity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        tv = (TextView) findViewById(R.id.splash_text);
        final Animation anim = AnimationUtils.loadAnimation(getBaseContext(),android.R.anim.fade_in);
        final Animation animOut = AnimationUtils.loadAnimation(getBaseContext(),android.R.anim.fade_out);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                tv.startAnimation(animOut);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tv.startAnimation(anim);
    }

}
