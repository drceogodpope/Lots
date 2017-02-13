package com.heapdragon.lots;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.*;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;

public class SiteActivity extends AppCompatActivity {

    private static final String TAG = "SiteActivityTAG";

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView mToolbarTitle;
    private String key;
    private DatabaseReference mSitesRef;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.activity_site);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mSitesRef = mRootRef.child(SITES_NODE);
        mToolbarTitle = (TextView) findViewById(R.id.site_activity_toolbar_title);
        mToolbarTitle.setText(getIntent().getStringExtra("name"));

        key = getIntent().getStringExtra("key");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new SiteFragAdapter(getSupportFragmentManager(),key));
        mViewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.site_activity_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG,"onTabSelected() "+ tab);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(getIntent().getIntExtra("color",0));
        toolbar.setBackgroundColor((getIntent().getIntExtra("color",0)));
        getWindow().setStatusBarColor(Utility.darker(getIntent().getIntExtra("color",0),0.8f));
        mViewPager.setCurrentItem(getIntent().getIntExtra("adapterPage",0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_site:
                android.util.Log.d(TAG,LOTS_NODE_PREFIX+key);
                deleteSite();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSite() {
        mSitesRef.child(key).removeValue();
        mRootRef.child(LOTS_NODE_PREFIX+key).removeValue();
        mRootRef.child(LOG_NODE_PREFIX+key).removeValue();
        FirebaseStorage.getInstance().getReference().child(SITE_MAPS_ROOT).child(key).delete();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
