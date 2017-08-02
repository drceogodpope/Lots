package com.heapdragon.lots;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Francesco on 2017-07-15.
 */

public class MaterialDotFragment extends StatusDotFragment {

    public static MaterialDotFragment newInstance(String siteKey,int lotNumber){
        MaterialDotFragment instance = new MaterialDotFragment();
        Bundle args = new Bundle();
        args.putString("siteKey",siteKey);
        args.putInt("lotNumber",lotNumber);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected void setDatabaseReference() {
        innerDot.setVisibility(View.GONE);
        dbRef = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.LOTS_NODE_PREFIX+siteKey).child(String.valueOf(lotNumber));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Boolean materialOrdered = (boolean) dataSnapshot.child(DataBaseConstants.LOTS_MATERIAL_ORDERED).getValue();
                    setStatusText(materialOrdered);
                    number.setText(dataSnapshot.getKey());
                    if(materialOrdered){
                        outterDot.setColor(R.color.colorGreen);
                    }
                    else{
                        outterDot.setColor(R.color.colorRed);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setStatusText(Object value) {
        if((boolean)value)statusText.setText("Material Ordered");
        else statusText.setText("Lot Not Prepared");
    }
}
