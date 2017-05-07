package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;

public abstract class LotStatusFragment extends Fragment {
    protected String key;
    protected String nodeKey;
    protected int lotNumber;
    protected TextView status;
    protected LotDot innerDot,outterDot;
    protected TextView number;
    protected TextView b0,b1,b2,b3;
    protected DatabaseReference statusRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
        nodeKey = getArguments().getString("nodeKey");
        lotNumber = getArguments().getInt("lotNumber");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lot_status_e, container, false);
        status = (TextView) view.findViewById(R.id.status);
        innerDot = (LotDot) view.findViewById(R.id.inner_dot);
        outterDot = (LotDot) view.findViewById(R.id.outter_dot);
        number = (TextView) view.findViewById(R.id.status_dot_number);
        b0 = (TextView) view.findViewById(R.id.b0);
        b1 = (TextView) view.findViewById(R.id.b1);
        b2 = (TextView) view.findViewById(R.id.b2);
        b3 = (TextView) view.findViewById(R.id.b3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setNodeKey();
        statusRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+key).child(String.valueOf(lotNumber)).child(nodeKey);
        addFirebaseListener();
        number.setText(String.valueOf(lotNumber));
        setButtons(statusRef);
    }


    private void addFirebaseListener(){
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value = (long)dataSnapshot.getValue();
                int value1 = (int) value;
                changeLotDot(value1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void setStatus(int status){
        statusRef.setValue(status);
        createLog(status);
    }

    protected void createLog(int status) {
        DatabaseReference logRef = FirebaseDatabase.getInstance().getReference().child(LOG_NODE_PREFIX+key);
        Map<String,Object> map = new HashMap<>();
        map.put(LOG_NUMBER,lotNumber);
        map.put(LOG_STATUS,status);
        map.put(LOG_TIME_STAMP,new DateTime().getMillis());
        logRef.push().setValue(map);
    }

    protected abstract void changeLotDot(int value1);
    protected abstract void setNodeKey();
    protected abstract void setButtons(DatabaseReference statusRef);
}
