package com.heapdragon.lots;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Francesco on 2017-01-15.
 */

public class ViewHelper {

    public static void changeColourAnim(int colorFrom, int colorTo, final View view){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}
