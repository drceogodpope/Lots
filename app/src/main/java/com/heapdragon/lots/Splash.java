package com.heapdragon.lots;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class Splash extends Activity {
    TextView tv;
    FloatingActionButton ring2;
    FloatingActionButton ring3;
    FloatingActionButton ring4;
    FloatingActionButton ring1;
    RelativeLayout circlesRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        setContentView(R.layout.splash);
        tv = (TextView) findViewById(R.id.splash_text);
        ring1 = (FloatingActionButton) findViewById(R.id.splash_ring1);
        ring2 = (FloatingActionButton) findViewById(R.id.splash_ring2);
        ring4 = (FloatingActionButton) findViewById(R.id.splash_ring4);
        ring3 = (FloatingActionButton) findViewById(R.id.splash_ring3);

        circlesRoot = (RelativeLayout) findViewById(R.id.root_circles);
        final int color1 = ContextCompat.getColor(getApplicationContext(), R.color.colorAmber);
        final int color3 = ContextCompat.getColor(getApplicationContext(), R.color.colorBlue2);
        final int color2 = ContextCompat.getColor(getApplicationContext(), R.color.colorRed);
        final int color4 = ContextCompat.getColor(getApplicationContext(), R.color.colorGreen);
        final int color5 = ContextCompat.getColor(getApplicationContext(), R.color.colorPurple1);

        final int color = ContextCompat.getColor(getApplicationContext(), R.color.colorDarkBlueGrey);
        ring1.setBackgroundTintList(ColorStateList.valueOf(color2));
        ring3.setBackgroundTintList(ColorStateList.valueOf(color2));
        ring2.setBackgroundTintList(ColorStateList.valueOf(color2));
        ring4.setBackgroundTintList(ColorStateList.valueOf(color2));


        final ArrayList<Integer> colorsOdd = new ArrayList<>();
        final ArrayList<Integer> colorsEven = new ArrayList<>();
        colorsEven.add(color2);
        colorsEven.add(color4);
        colorsEven.add(color1);
        colorsEven.add(color5);
        colorsEven.add(color1);

        colorsOdd.add(color2);
        colorsOdd.add(color5);
        colorsOdd.add(color3);
        colorsOdd.add(color);


        final Animation hover = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.hover);
        circlesRoot.startAnimation(hover);
        hover.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ViewHelper.changeFabMultiColor(colorsOdd, ring1, 1000);
                ViewHelper.changeFabMultiColor(colorsEven, ring2, 1000);
                ViewHelper.changeFabMultiColor(colorsOdd, ring3, 1000);
                ViewHelper.changeFabMultiColor(colorsEven, ring4, 1000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Splash.this.startActivity(new Intent(Splash.this, SitesActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        circlesRoot.startAnimation(hover);


    }
}
