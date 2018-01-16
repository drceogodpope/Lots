package com.heapdragon.lots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArchDotFragment extends StatusDotFragment implements Deactivatable {
    private static String TAG = "ArchDotFragment";
    private ValueAnimator valueAnimator;
    private Context mContext;
    private ArrayList<Integer> flashyColors;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        flashyColors = new ArrayList<>();
        mContext = getContext();
    }

    public static ArchDotFragment newInstance(String siteKey,int lotNumber){
        ArchDotFragment instance = new ArchDotFragment();
        Bundle args = new Bundle();
        args.putString("siteKey",siteKey);
        args.putInt("lotNumber",lotNumber);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected void setDatabaseReference() {
        dbRef = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX+siteKey).child(String.valueOf(lotNumber));
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long archStatus = (long) dataSnapshot.child(DataBaseConstants.LOTS_ARCH_STATUS).getValue();
                    boolean archOrdered = (boolean) dataSnapshot.child(DataBaseConstants.LOTS_ARCH_ORDERED).getValue();
                    setStatusText(archStatus);
                    number.setText(dataSnapshot.getKey());
                    setDotColor(archStatus);
                    if(archOrdered){
//                        flashyColors.clear();
//                        flashyColors.add(outterDot.getColorStateList().getDefaultColor());
//                        flashyColors.add(ContextCompat.getColor(mContext,R.color.colorGreen));
//                        if(valueAnimator==null){
//                            valueAnimator = new ColorAnimFactory(mContext).flashingView(flashyColors, outterDot, 1000);
//                            valueAnimator.start();
                        outterDot.startFlashing();
                    }
                    else{
                        outterDot.endFlashing();
                        setDotColor(archStatus);

//                        if(valueAnimator!=null){
//                            Log.d(TAG,"valueAnimator!=null");
//                            ArchDotFragment.this.valueAnimator.end();
//                            valueAnimator = null;
//                        }
//                        else{
//                            Log.d(TAG,"valueAnimator==null");
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        innerDot.setColor(R.color.colorDarkBackground);
    }

    @Override
    public void setStatusText(Object value) {
        long value_ = (long) value;
        if(value_==0){
            statusText.setText("Work Order Required");
        }
        if(value_==1){
            statusText.setText("Arch In Production");
        }
        if(value_==2){
            statusText.setText("Arch In Shipping");
        }
    }

    @Override
    public void deactivate() {
        cardView.setVisibility(View.GONE);
    }

    @Override
    public void activate() {
        cardView.setVisibility(View.VISIBLE);
    }

    public void setDotColor(long archStatus) {
        if(archStatus==0){
            outterDot.setColor(R.color.colorPurple1);
        }
        if(archStatus==1){
            outterDot.setColor(R.color.colorAmber);
        }
        if(archStatus==2){
            outterDot.setColor(R.color.colorBlue3);
        }
    }
}
