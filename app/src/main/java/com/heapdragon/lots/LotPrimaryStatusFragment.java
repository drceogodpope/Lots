package com.heapdragon.lots;

import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX;


/**
 * Created by Francesco on 2017-05-04.
 */

public class LotPrimaryStatusFragment extends LotStatusFragment {


    @Override
    protected void setNodeKey() {
        this.nodeKey = DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX;
    }

    @Override
    protected void changeLotDot(int value1) {
        outterDot.setVisibility(View.GONE);
        switch (value1){
            case Lot.ISSUE:
                innerDot.setColor(R.color.colorBlue);
                status.setText("Issue");
                break;
            case Lot.READY:
                innerDot.setColor(R.color.colorGreen);
                status.setText("Ready");
                break;
            case Lot.RECEIVED:
                innerDot.setColor(R.color.colorGrey);
                status.setText("Received");
                break;
            default:
                innerDot.setColor(R.color.colorRed);
                status.setText("Not Ready");
        }
    }

    @Override
    protected void setButtons(final DatabaseReference statusRef) {

        b0.setText("Ready\n");
        b1.setText("Not Ready\n");
        b2.setText("Received\n");
        b3.setText("Issue\n");


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
                setStatus(Lot.ISSUE);
            }
        });
    }


}
