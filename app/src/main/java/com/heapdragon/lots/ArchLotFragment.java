package com.heapdragon.lots;
import android.app.Activity;
import android.content.Context;
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
    private RelativeLayout archLotFrame;
    Activator mActivity;

    @Override
    public void deactivate() {

    }

    @Override
    public void activate() {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("siteKey");
        lotNumber = getArguments().getInt("lotNumber");
        rootRef = FirebaseDatabase.getInstance().getReference();
        lotRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber));
        archLotRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_ARCH_LOT).getRef();
        archOrderedRef = rootRef.child(DataBaseConstants.LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_ARCH_ORDERED).getRef();
        Log.d(TAG,"siteKey: " +key + " lotNumber: " + String.valueOf(lotNumber));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arch_lot,container,false);
        archOrdered = (CheckBox) view.findViewById(R.id.arch_ordered_button);
        archOrdered.setHighlightColor(ContextCompat.getColor(getContext(),R.color.colorGreen));
        archLotSwitch = (Switch) view.findViewById(R.id.arch_lot_switch);
        archLotFrame = (RelativeLayout) view.findViewById(R.id.arch_lot_frame);
        archLotSwitch.setOnCheckedChangeListener(this);
        archOrdered.setOnCheckedChangeListener(this);
        setButtons();
        return view;
    }

    private void setButtons() {
        lotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((boolean)dataSnapshot.child(DataBaseConstants.LOTS_ARCH_ORDERED).getValue()){
                    archOrdered.setChecked(true);
                }
                else {
                    archOrdered.setChecked(false);
                }
                if((boolean)dataSnapshot.child(DataBaseConstants.LOTS_ARCH_LOT).getValue()){
                    archLotSwitch.setChecked(true);
                    activateArchFrag();
                }
                else{
                    archLotSwitch.setChecked(false);
                    deactivateArchFrag();
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
                activateArchFrag();
            }
            else{
                Log.d(TAG,"!isChecked");
                archLotRef.setValue(false);
                deactivateArchFrag();
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

    //CALL WHEN ARCHLOTSWITCH IS SET TO FALSE
    private void deactivateArchFrag(){
        Log.d(TAG,"deactivateArchFrag()");
        archOrdered.setVisibility(View.GONE);
//      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)archLotSwitch.getLayoutParams();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(archLotSwitch.getLayoutParams());

        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        archLotSwitch.setLayoutParams(params);
        mActivity.deactivate();
    }

    private void activateArchFrag(){
        archOrdered.setVisibility(View.VISIBLE);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)archLotSwitch.getLayoutParams();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(archLotSwitch.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_LEFT,R.id.arch_lot_frame);
        archLotSwitch.setLayoutParams(params);
        mActivity.activate();
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
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
