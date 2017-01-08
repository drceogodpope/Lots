package com.heapdragon.lots;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

public class LotActivity extends AppCompatActivity {
    private static final String TAG = "LotActivity";

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mSitesRef = mRootRef.child(SITES_NODE);
    private DatabaseReference mlotsRef;

    private String key;
    private int lotNumber;
    private Button readyButton;
    private Button notReadyButton;
    private Button issueButton;
    private Button receivedButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);

        key = getIntent().getStringExtra("key");
        lotNumber = getIntent().getIntExtra("lotNumber",-1);
        mlotsRef  = mRootRef.child(LOTS_NODE_PREFIX+key);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(TAG,"fragmentManager.beginTransaction()");
        fragmentManager.beginTransaction().add(R.id.log_layout,LogFragment.newInstance(key)).commit();

        readyButton = (Button) findViewById(R.id.ready_button);
        notReadyButton = (Button) findViewById(R.id.not_ready_button);
        issueButton = (Button) findViewById(R.id.issue_button);
        receivedButton = (Button) findViewById(R.id.received_button);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.READY);
            }
        });

        issueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.ISSUE);
            }
        });

        notReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.NOT_READY);
            }
        });

        receivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatus(Lot.RECEIVED);
            }
        });

        final TextView siteName = (TextView) findViewById(R.id.site_name_and_number);
        mSitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                siteName.setText((dataSnapshot.child(key).child(NAME_NODE).getValue()) + " - " + lotNumber);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Couldn't get site name ...",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setStatus(int status){
        mlotsRef.child(String.valueOf(lotNumber)).setValue(status);
        createLog(status);
        onBackPressed();
    }

    private void createLog(int status) {
        DatabaseReference logRef = mRootRef.child(LOG_NODE_PREFIX+key);
        String logKey = logRef.push().getKey();
        logRef.child(logKey).child(LOG_NUMBER).setValue(lotNumber);
        logRef.child(logKey).child(LOG_TIME_STAMP).setValue(new DateTime().getMillis());
        logRef.child(logKey).child(LOG_STATUS).setValue(status);
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
