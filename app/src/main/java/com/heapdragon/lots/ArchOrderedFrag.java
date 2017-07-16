package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArchOrderedFrag extends Fragment {
    private RadioButton workOrderRequired;
    private RadioButton archInProduction;
    private RadioButton archInShipping;
    private RadioGroup radioGroup;
    private String siteKey;
    private int lotNumber;
    private DatabaseReference lotRef;
    private DatabaseBitch dbBitch;

    public static ArchOrderedFrag newInstance(String sitekey,int lotNumber){
        Bundle args = new Bundle();
        args.putInt("lotNumber",lotNumber);
        args.putString("siteKey",sitekey);
        ArchOrderedFrag archOrderedFrag = new ArchOrderedFrag();
        archOrderedFrag.setArguments(args);
        return archOrderedFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arch_status,container,false);
        workOrderRequired = (RadioButton) view.findViewById(R.id.work_order_required);
        archInProduction = (RadioButton) view.findViewById(R.id.arch_in_production);
        archInShipping = (RadioButton) view.findViewById(R.id.arch_in_shipping);
        radioGroup = (RadioGroup) view.findViewById(R.id.arch_status_radio_group);
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

    private void handleButtonClick(RadioButton rb) {
        radioGroup.check(rb.getId());
        if(rb.getId() == workOrderRequired.getId()){
            setArchStatus(0);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,0);
        }
        if(rb.getId() == archInProduction.getId()){
            setArchStatus(1);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,1);
        }
        if(rb.getId() == archInShipping.getId()){
            setArchStatus(2);
            dbBitch.createLog2(siteKey,lotNumber,DataBaseConstants.LOTS_ARCH_STATUS,2);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.siteKey = getArguments().getString("siteKey");
        this.lotNumber = getArguments().getInt("lotNumber");
        lotRef = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX+siteKey).child(String.valueOf(lotNumber));
        dbBitch = new DatabaseBitch();
    }

    private void setArchStatus(int status){
        lotRef.child(DataBaseConstants.LOTS_ARCH_STATUS).setValue(status);
    }

}
