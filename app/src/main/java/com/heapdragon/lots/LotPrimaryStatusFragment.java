package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class LotPrimaryStatusFragment extends LotStatusFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setNodeKey() {
        this.nodeKey = DataBaseConstants.LOTS_MATERIAL_ORDERED;
    }

    @Override
    protected void changeLotDot(int value1) {
        outerDot.setVisibility(View.INVISIBLE);
        switch (value1){
            case Lot.ISSUE:
                innerDot.setColor(R.color.colorIssue);
                status.setText("Issue");
                break;
            case Lot.READY:
                innerDot.setColor(R.color.colorReady);
                status.setText("Ready");
                break;
            case Lot.RECEIVED:
                innerDot.setColor(R.color.colorReceived);
                status.setText("Received");
                break;
            default:
                innerDot.setColor(R.color.colorNotReady);
                status.setText("Not Ready");
        }
    }

    @Override
    protected void setButtons(final DatabaseReference statusRef) {
        b0.setBackgroundColor(getResources().getColor(R.color.colorReady));
        b1.setBackgroundColor(getResources().getColor(R.color.colorNotReady));
        b2.setBackgroundColor(getResources().getColor(R.color.colorReceived));
        b3.setBackgroundColor(getResources().getColor(R.color.colorIssue));
        b0.setText("Ready to Receive");
        b1.setText("Not Ready to Receive");
        b2.setText("Materials Received");
        b3.setText("New Lot \n Issue");


        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.READY);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.NOT_READY);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.RECEIVED);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UNCOMMENT ME FOR FINAL BUILD
//                setStatus(Lot.ISSUE);
                Toast.makeText(getContext(),"Feature not available ... yet",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
