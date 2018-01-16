package com.heapdragon.lots;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by Francesco on 2017-08-03.
 */

 class ColorAnimFactory {


    static ValueAnimator flashingCompoundButtonVar(ArrayList<Integer> colors,final CompoundButton view, int duration){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),colors.toArray());
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setButtonTintList(ColorStateList.valueOf((int)animator.getAnimatedValue()));
            }
        });
        return colorAnimation;
    }

    static ValueAnimator flashingView(ArrayList<Integer> colors,final View view, int duration){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),colors.toArray());
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundTintList(ColorStateList.valueOf((int)animator.getAnimatedValue()));
            }
        });
        return colorAnimation;
    }

}
