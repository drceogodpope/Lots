package com.heapdragon.lots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

//        //        SIMULATION LIST OF SITES
//        ArrayList<Site> simulationSites = new ArrayList<>();
//        simulationSites.add(new Site("Lotus Pointe","sdfs",4,2,2,2,2));
//        simulationSites.add(new Site("Firglen Ridge","sdfs",4,2,2,2,0));
//        simulationSites.add(new Site("Pudget Sound","sdfs",4,2,2,2,0));
//        simulationSites.add(new Site("Kiloran Avenue","sdfs",4,2,2,2,2));
//        simulationSites.add(new Site("Postview Court","sdfs",4,2,2,2,2));
//        //        SIMULATION LIST OF SITES



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
                    String name = ds.child(NAME_NODE).getValue().toString();
                    int numberOfLots = Integer.valueOf(ds.child(TOTAL_LOTS_NODE).getValue().toString());
                    MainActivity.this.sites.add(new Site(name,numberOfLots));
                }
                MainActivity.this.mSiteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mSitesRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSiteActivity();
                mSitesRef.setValue("sites");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_site, menu);
        return true;
    }

    public void startSiteActivity(){
        Intent intent = new Intent(MainActivity.this,SiteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_new_site:
                Intent intent = new Intent(getApplicationContext(),AddSiteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
