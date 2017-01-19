package com.heapdragon.lots;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.heapdragon.lots.DataBaseConstants.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityTag";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mSitesRef = mRootRef.child(SITES_NODE);
    private RecyclerView mSitesRecyclerView;
    private SiteAdapter mSiteAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Site> sites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        mSitesRecyclerView = (RecyclerView) findViewById(R.id.site_activity_sites_recycler_view);
        mSitesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSiteAdapter = new SiteAdapter(sites);
        mSitesRecyclerView.setAdapter(mSiteAdapter);

        mSitesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sites.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    try{
                        String name = ds.child(NAME_NODE).getValue().toString();
                        int numberOfLots = Integer.valueOf(ds.child(TOTAL_LOTS_NODE).getValue().toString());
                        int incompleteLots = Integer.valueOf(ds.child(INCOMPLETE_LOTS_NODE).getValue().toString());
                        int issueLots = Integer.valueOf(ds.child(ISSUE_LOTS_NODE).getValue().toString());
                        int readyLots = Integer.valueOf(ds.child(READY_LOTS_NODE).getValue().toString());
                        int receivedLots = Integer.valueOf(ds.child(RECEIVED_LOTS_NODE).getValue().toString());
                        int siteColor = Integer.valueOf(ds.child(SITE_COLOR_NODE).getValue().toString());
                        String id = ds.getKey();
                        MainActivity.this.sites.add(new Site(name,numberOfLots,incompleteLots,issueLots,readyLots,receivedLots,siteColor,id));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                MainActivity.this.mSiteAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        switch (item.getItemId()) {
            case R.id.create_new_site:
                Intent intent = new Intent(getApplicationContext(),AddSiteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
