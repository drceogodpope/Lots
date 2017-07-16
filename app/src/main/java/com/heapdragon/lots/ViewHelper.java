package com.heapdragon.lots;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

public class ViewHelper {

    private static final String TAG = "ViewHelper";
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

    public static void changeColourTimeAnim(int colorFrom, int colorTo, final View view,int durationMilli){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(durationMilli); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public static void changeFABColor(int colorFrom, int colorTo, final FloatingActionButton fab, int durationMilli){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(durationMilli); // milliseconds
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                fab.setBackgroundTintList(ColorStateList.valueOf((int)animator.getAnimatedValue()));
            }

        });
        colorAnimation.start();
    }
    public static void changeFabMultiColor(final ArrayList<Integer> colors, final FloatingActionButton fab, final int durationMilli){

           if(colors.size()>1){

               ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colors.get(0), colors.get(1));
               Log.d(TAG,"changeFabMultiColor()");
               Log.d(TAG,"colors: ");
               for(int i = 0;i<colors.size();i++){
                   Log.d(TAG,"colors["+String.valueOf(i)+"]: " +String.valueOf(colors.get(i)));
               }

               colorAnimation.setDuration(durationMilli); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        fab.setBackgroundTintList(ColorStateList.valueOf((int)animator.getAnimatedValue()));
                    }

                });
                colorAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ArrayList<Integer> colors_ = (ArrayList<Integer>) colors.clone();
                        colors_.remove(0);
                        changeFabMultiColor(colors_,fab,durationMilli);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                colorAnimation.start();
            }


    }
}
