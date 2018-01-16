package com.heapdragon.lots;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

interface Deactivatable {
    void deactivate();
    void activate();
}

public class ArchLotFragment extends Fragment implements Deactivatable,CompoundButton.OnCheckedChangeListener{
    private static String TAG = "ArchLotFragment";
    private String key;
    private int lotNumber;
    private Switch archLotSwitch;
    private CheckBox archOrdered;
    private DatabaseReference rootRef;
    private DatabaseReference archLotRef;
    private DatabaseReference lotRef;
    private DatabaseReference archOrderedRef;
    private ValueAnimator colorAnimation;
    private Context mContext;
    private ArrayList<Integer> flashyColors;
    Activator mActivity;

    @Override
    public void deactivate() {
        Log.d(TAG,"deactivateArchFrag()");
        archOrdered.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(archLotSwitch.getLayoutParams());
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        archLotSwitch.setLayoutParams(params);
        mActivity.deactivate();
    }

    @Override
    public void activate() {
        archOrdered.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(archLotSwitch.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_LEFT,R.id.arch_lot_frame);
        archLotSwitch.setLayoutParams(params);
        mActivity.activate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ArchLotFragment newInstance(String siteKey, int lotNumber){
        ArchLotFragment archLotFragment = new ArchLotFragment();
        Bundle args = new Bundle();
        args.putInt("lotNumber",lotNumber);
        args.putString("siteKey",siteKey);
        archLotFragment.setArguments(args);
        return archLotFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getContext();
        key = getArguments().getString("siteKey");
        lotNumber = getArguments().getInt("lotNumber");
        rootRef = FirebaseDatabase.getInstance().getReference();
        lotRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber));
        archLotRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_ARCH_LOT).getRef();
        archOrderedRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_ARCH_ORDERED).getRef();
        Log.d(TAG,"siteKey: "+key+" lotNumber: "+String.valueOf(lotNumber));

        flashyColors = new ArrayList<>();
        flashyColors.add(ContextCompat.getColor(mContext,R.color.colorPurple1));
        flashyColors.add(ContextCompat.getColor(mContext,R.color.colorAmber));
        flashyColors.add(ContextCompat.getColor(mContext,R.color.colorBlue));
        flashyColors.add(ContextCompat.getColor(mContext,R.color.colorGreen));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arch_lot,container,false);
        archOrdered = (CheckBox) view.findViewById(R.id.arch_ordered_button);
        archOrdered.setHighlightColor(ContextCompat.getColor(getContext(),R.color.colorGreen));
        archLotSwitch = (Switch) view.findViewById(R.id.arch_lot_switch);
        archLotSwitch.setOnCheckedChangeListener(this);
        archOrdered.setOnCheckedChangeListener(this);
        archOrdered.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.colorGreen)));
        setButtons();
        return view;
    }

    private void setButtons() {
        lotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(DataBaseConstants.LOTS_ARCH_ORDERED).getValue()!=null){
                    if((boolean)dataSnapshot.child(DataBaseConstants.LOTS_ARCH_ORDERED).getValue()){
                        if(colorAnimation==null){
                            ArchLotFragment.this.colorAnimation = ColorAnimFactory.flashingCompoundButtonVar(flashyColors,archOrdered,2000);
                        }
                        colorAnimation.start();
                        archOrdered.setChecked(true);
                    }
                    else {
                        archOrdered.setChecked(false);
                        if(colorAnimation!=null){
                            ArchLotFragment.this.colorAnimation.end();
                        }
                        archOrdered.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.colorGreen)));
                    }
                    if((boolean)dataSnapshot.child(DataBaseConstants.LOTS_ARCH_LOT).getValue()){
                        archLotSwitch.setChecked(true);
                        activate();
                    }
                    else{
                        archLotSwitch.setChecked(false);
                        deactivate();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if(compoundButton.getId() == archLotSwitch.getId()){
            if (isChecked) {
                Log.d(TAG,"isChecked");
                archLotRef.setValue(true);
                activate();
            }
            else{
                Log.d(TAG,"!isChecked");
                archLotRef.setValue(false);
                deactivate();
            }
        }
        else {
            if (isChecked) {
                Log.d(TAG,"isChecked");
                archOrderedRef.setValue(true);
            }
            else{
                Log.d(TAG,"!isChecked");
                archOrderedRef.setValue(false);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mActivity = (Activator)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Activator interface");
        }
    }
}
