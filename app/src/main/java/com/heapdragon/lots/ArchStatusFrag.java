package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

interface SimpleCallBack<T>{
    void simpleCallBack(T data);
}

public class ArchStatusFrag extends Fragment implements SimpleCallBack,Deactivatable{
    private static final String TAG = "ArchStatusFrag";
    private RadioButton workOrderRequired;
    private RadioButton archInProduction;
    private RadioButton archInShipping;
    private String siteKey;
    private int lotNumber;
    private DatabaseReference lotRef;
    private DatabaseBitch dbBitch;
    private CardView root;


    public static ArchStatusFrag newInstance(String siteKey, int lotNumber){
        Bundle args = new Bundle();
        args.putInt("lotNumber",lotNumber);
        args.putString("siteKey",siteKey);
        ArchStatusFrag archStatusFrag = new ArchStatusFrag();
        archStatusFrag.setArguments(args);
        return archStatusFrag;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arch_status,container,false);
        workOrderRequired = (RadioButton) view.findViewById(R.id.work_order_required);
        archInProduction = (RadioButton) view.findViewById(R.id.arch_in_production);
        archInShipping = (RadioButton) view.findViewById(R.id.arch_in_shipping);
        root = (CardView) view.findViewById(R.id.arch_status_root);
        workOrderRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(workOrderRequired);
            }
        });
        archInShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(archInShipping);
            }
        });
        archInProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(archInProduction);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    private void handleButtonClick(RadioButton rb) {
        Log.d(TAG,"handleButtonClick()");
        if(rb.getId() == workOrderRequired.getId()){
            workOrderRequired.setChecked(true);
            archInProduction.setChecked(false);
            archInShipping.setChecked(false);
            setArchStatus(Lot.WORK_ORDER_REQUIRED);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,0);
        }
        if(rb.getId() == archInProduction.getId()){
            workOrderRequired.setChecked(false);
            archInProduction.setChecked(true);
            archInShipping.setChecked(false);
            setArchStatus(Lot.ARCH_IN_PRODUCTION_1);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,1);
        }
        if(rb.getId() == archInShipping.getId()){
            workOrderRequired.setChecked(false);
            archInProduction.setChecked(false);
            archInShipping.setChecked(true);
            setArchStatus(Lot.ARCH_IN_SHIPPING_1);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,2);
        }
    }

    private void setButtonValues(RadioButton rb) {
        Log.d(TAG,"handleButtonClick()");
        if(rb.getId() == workOrderRequired.getId()){
            workOrderRequired.setChecked(true);
            archInProduction.setChecked(false);
            archInShipping.setChecked(false);
        }
        if(rb.getId() == archInProduction.getId()){
            workOrderRequired.setChecked(false);
            archInProduction.setChecked(true);
            archInShipping.setChecked(false);
        }
        if(rb.getId() == archInShipping.getId()){
            workOrderRequired.setChecked(false);
            archInProduction.setChecked(false);
            archInShipping.setChecked(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.siteKey = getArguments().getString("siteKey");
        this.lotNumber = getArguments().getInt("lotNumber");
        lotRef = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX+siteKey).child(String.valueOf(lotNumber));
        dbBitch = new DatabaseBitch();
        DatabaseReference lotReference = lotRef.child(DataBaseConstants.LOTS_ARCH_STATUS);

        lotReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object check = dataSnapshot.getValue();
                if(check!=null){
                    long value = (long)dataSnapshot.getValue();
                    simpleCallBack(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"CANCELLED");
            }
        });
    }

    private void setArchStatus(long status){
        lotRef.child(DataBaseConstants.LOTS_ARCH_STATUS).setValue(status);
    }

    @Override
    public void simpleCallBack(Object data) {
        long value = (long) data;
        if (value==Lot.WORK_ORDER_REQUIRED) {
            Log.d(TAG,String.valueOf(value) + "==" +String.valueOf(Lot.WORK_ORDER_REQUIRED));
            setButtonValues(workOrderRequired);
        }
        if (value==Lot.ARCH_IN_PRODUCTION_1) {
            Log.d(TAG,String.valueOf(value) + "==" +String.valueOf(Lot.ARCH_IN_PRODUCTION_1));
            setButtonValues(archInProduction);
        }
        if (value==Lot.ARCH_IN_SHIPPING_1) {
            Log.d(TAG,String.valueOf(value) + "==" +String.valueOf(Lot.ARCH_IN_SHIPPING_1));
            setButtonValues(archInShipping);
        }
    }

    public void activate(){
        root.setVisibility(View.VISIBLE);
    }

    public void deactivate(){
        root.setVisibility(View.GONE);
    }
}
