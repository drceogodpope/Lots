package com.heapdragon.lots;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import static com.heapdragon.lots.DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

public class LotActivity extends AppCompatActivity {

    private static final String TAG = "LotActivity";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mSitesRef = mRootRef.child(SITES_NODE);
    private DatabaseReference mLotsRef;
    private String key;
    private int lotNumber;
    private Button readyButton;
    private Button notReadyButton;
    private Button issueButton;
    private Button receivedButton;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);

        key = getIntent().getStringExtra("siteKey");
        lotNumber = getIntent().getIntExtra("lotNumber",-1);
        mLotsRef = mRootRef.child(LOTS_NODE_PREFIX+key);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(TAG,"fragmentManager.beginTransaction()");
        fragmentManager.beginTransaction().add(R.id.log_layout, LotLogFragment.newInstance(key,lotNumber)).commit();

        readyButton = (Button) findViewById(R.id.ready_button);
        notReadyButton = (Button) findViewById(R.id.not_ready_button);
        issueButton = (Button) findViewById(R.id.issue_button);
        receivedButton = (Button) findViewById(R.id.received_button);
        final TextView siteName = (TextView) findViewById(R.id.site_name_and_number);

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrimaryStatus(Lot.READY);
            }
        });
        issueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrimaryStatus(Lot.ISSUE);
            }
        });
        notReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrimaryStatus(Lot.NOT_READY);
            }
        });
        receivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrimaryStatus(Lot.RECEIVED);
            }
        });

        mSitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Log.d(TAG,"SITE KEY: " + key);
                    Log.d(TAG,"SITE NAME: " + dataSnapshot.child(key).child(NAME_NODE).getValue().toString());

                    String name = (dataSnapshot.child(key).child(NAME_NODE).getValue()).toString();
                    siteName.setText( name + " - " + lotNumber);
                }catch (Exception e){
                    Log.d(TAG,e.toString());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Couldn't get site name ...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPrimaryStatus(int status){
        mLotsRef.child(String.valueOf(lotNumber)).child(LOTS_PRIMARY_STATUS_PREFIX).setValue(status);
        createLog(status);
        changeLotColor(status);
    }

    private void changeLotColor(int status) {
        int[] statusColors = getApplicationContext().getResources().getIntArray(R.array.statusColors);
        ViewHelper.changeColourAnim(((ColorDrawable) toolbar.getBackground()).getColor(),statusColors[status],toolbar);
    }

    private void createLog(int status) {
        DatabaseReference logRef = mRootRef.child(LOG_NODE_PREFIX+key);
        Map<String,Object> map = new HashMap<>();
        map.put(LOG_NUMBER,lotNumber);
        map.put(LOG_STATUS,status);
        map.put(LOG_TIME_STAMP,new DateTime().getMillis());
        logRef.push().setValue(map);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
