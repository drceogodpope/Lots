package com.heapdragon.lots;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

/**
 * Created by Francesco on 2017-01-06.
 */

public class LotActivity extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mSitesRef = mRootRef.child(SITES_NODE);
    private String key;
    private int lotNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);

        key = getIntent().getStringExtra("key");
        lotNumber = getIntent().getIntExtra("lotNumber",-1);

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
}
