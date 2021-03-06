package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

public class EnhancedLotActivity extends AppCompatActivity {

    private final static String TAG = "EnhancedLotActivity";
    private String key;
    private int lotNumber;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GET SITE KEY AND LOTNUMBER//
        key = getIntent().getStringExtra("siteKey");
        lotNumber = getIntent().getIntExtra("lotNumber",0);
        setContentView(R.layout.activity_lot_e);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setToolbarTitle();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new LotFragAdapter(getSupportFragmentManager(),key,lotNumber));
        viewPager.setOffscreenPageLimit(3);
        TabLayout tl = (TabLayout) findViewById(R.id.tab_layout);
        tl.setupWithViewPager(viewPager);

    }

    private void setToolbarTitle(){
        if(lotNumber!= 0){
            toolbarTitle = (TextView) findViewById(R.id.site_name_and_number);
            DatabaseReference mSitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE);
            mSitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG,"Key = " + key);
                    String name = (dataSnapshot.child(key).child(NAME_NODE).getValue()).toString();
                    toolbarTitle.setText( name + " - " + lotNumber);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EnhancedLotActivity.this, "Connection Error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

