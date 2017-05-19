package com.heapdragon.lots;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;

public abstract class LotStatusFragment extends Fragment {

    private static final String TAG = "LotStatusFragment";

    //MEMBER VARIABLES
    protected String key;
    protected String nodeKey;
    protected int lotNumber;
    protected DatabaseReference statusRef;
    protected DatabaseBitch databaseBitch;
    protected long statusLevel;

    //VIEWS
    protected TextView status;
    protected LotDot innerDot, outerDot;
    protected TextView number;
    protected TextView b0,b1,b2,b3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
        nodeKey = getArguments().getString("nodeKey");
        lotNumber = getArguments().getInt("lotNumber");
        databaseBitch = new DatabaseBitch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lot_status_e, container, false);
        status = (TextView) view.findViewById(R.id.status);
        innerDot = (LotDot) view.findViewById(R.id.inner_dot);
        outerDot = (LotDot) view.findViewById(R.id.outter_dot);
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
                try{
                    long value = (long)dataSnapshot.getValue();
                    int value1 = (int) value;
                    changeLotDot(value1);
                }
                catch (Exception e){
                    Log.d(TAG,e.toString());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void setStatus(int status){
        statusRef.setValue(status);
        databaseBitch.createLog(status,key,lotNumber,statusLevel);
    }

    protected abstract void changeLotDot(int value1);
    protected abstract void setNodeKey();
    protected abstract void setButtons(DatabaseReference statusRef);
}
