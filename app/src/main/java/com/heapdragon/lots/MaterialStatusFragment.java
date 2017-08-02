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
import static com.heapdragon.lots.DataBaseConstants.LOTS_MATERIAL_ORDERED;

public class MaterialStatusFragment extends Fragment {
    RadioButton lotNotPreparedButton;
    RadioButton materialOrderedButton;
    private String siteKey;
    private int lotNumber;
    RadioGroup radioGroup;
    private DatabaseBitch databaseBitch;

    public static MaterialStatusFragment newInstance(String siteKey,int lotNumber){
        MaterialStatusFragment materialStatusFragment = new MaterialStatusFragment();
        Bundle args = new Bundle();
        args.putString("siteKey",siteKey);
        args.putInt("lotNumber",lotNumber);
        materialStatusFragment.setArguments(args);
        return materialStatusFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_status,container,false);
        lotNotPreparedButton = (RadioButton) view.findViewById(R.id.material_fragment_lot_not_prepared);
        materialOrderedButton = (RadioButton) view.findViewById(R.id.material_fragment_material_ordered);
        radioGroup = (RadioGroup) view.findViewById(R.id.material_fragment_radio_group);
        lotNotPreparedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRadioClick(lotNotPreparedButton);
            }
        });
        materialOrderedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRadioClick(materialOrderedButton);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkCurrentStatus();

    }

    private void checkCurrentStatus() {
        DatabaseReference lotReference = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX + siteKey).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_MATERIAL_ORDERED);
        lotReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((boolean) dataSnapshot.getValue()) {
                    radioGroup.check(materialOrderedButton.getId());
                } else radioGroup.check(lotNotPreparedButton.getId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteKey = getArguments().getString("siteKey");
        lotNumber = getArguments().getInt("lotNumber");
        databaseBitch = new DatabaseBitch();
    }

    private void handleRadioClick(RadioButton rb){
        radioGroup.check(rb.getId());
        if(rb.equals(materialOrderedButton)){
            setMaterialOrdered(true);
            databaseBitch.createLog2(siteKey,lotNumber,LOTS_MATERIAL_ORDERED,true);

        }
        else{
            setMaterialOrdered(false);
            databaseBitch.createLog2(siteKey,lotNumber,LOTS_MATERIAL_ORDERED,false);
        }

    }

    private void setMaterialOrdered(boolean value){
        DatabaseReference lotReference = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX+siteKey).child(String.valueOf(lotNumber)).child(DataBaseConstants.LOTS_MATERIAL_ORDERED);
        lotReference.setValue(value);
    }

}
