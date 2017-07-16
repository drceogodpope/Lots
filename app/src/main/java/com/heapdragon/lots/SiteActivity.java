package com.heapdragon.lots;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_COLOR_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;

public class SiteActivity extends AppCompatActivity implements ColorChooserFrag.OnColorChosenListener {

    private static final String TAG = "SiteActivityTAG";

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView mToolbarTitle;
    private String key;
    private int color;
    private String name;
    private Toolbar toolbar;
    private DatabaseReference mSitesRef;
    private DatabaseReference mRootRef;
    private FrameLayout colorPickerFragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");

        //SET CONTENT VIEW AND TOOLBAR//
        setContentView(R.layout.activity_site);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //FIND VIEWS//
        colorPickerFragLayout = (FrameLayout) findViewById(R.id.colorPickerFragLayout);
        colorPickerFragLayout.setVisibility(View.GONE);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mSitesRef = mRootRef.child(SITES_NODE);
        mToolbarTitle = (TextView) findViewById(R.id.site_activity_toolbar_title);
        mToolbarTitle.setText(getIntent().getStringExtra("name"));


        //GET EXTRAS//
        key = getIntent().getStringExtra("key");
        color = getIntent().getIntExtra("color",0);
        name = getIntent().getStringExtra("name");
        Log.d(TAG,   "key: " + key + "\n"
                    +"color: " + String.valueOf(color) + "\n"
                    +"name: " + name);

        //SET UP VIEW PAGER//
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
//        tabLayout.setBackgroundColor(getIntent().getIntExtra("color",0));
//        toolbar.setBackgroundColor((getIntent().getIntExtra("color",0)));
        setColors(color);
        getWindow().setStatusBarColor(Utility.darker(getIntent().getIntExtra("color",0),0.8f));
        mViewPager.setCurrentItem(getIntent().getIntExtra("adapterPage",0));


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_in)
                .add(R.id.colorPickerFragLayout,ColorChooserFrag.newInstance())
                .commit();

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(colorPickerFragLayout.getVisibility()==View.VISIBLE){
                    colorPickerFragLayout.setVisibility(View.GONE);
                }
                else {
                    colorPickerFragLayout.setVisibility(View.VISIBLE);
                }
                  return false;
            }
        });
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
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        switch (choice) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteSite();
                                onBackPressed();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(SiteActivity.this);
                builder.setMessage("Delete site? This can not be undone!")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            case R.id.delete_site_map:

                DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        switch (choice) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteSiteMap();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SiteActivity.this);
                builder1.setMessage("Delete map? This can not be undone!")
                        .setPositiveButton("Yes", dialogClickListener2)
                        .setNegativeButton("No", dialogClickListener2).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSite() {
        mSitesRef.child(key).removeValue();
        mRootRef.child(LOTS_NODE_PREFIX+key).removeValue();
        mRootRef.child(LOG_NODE_PREFIX+key).removeValue();
        deleteSiteMap();
    }

    private void deleteSiteMap(){
        FirebaseStorage.getInstance().getReference().child(SITE_MAPS_ROOT).child(key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,SitesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onColorChosen(int color) {
        setColors(color);
        mSitesRef.child(key).child(SITE_COLOR_NODE).setValue(color);
        colorPickerFragLayout.setVisibility(View.GONE);
    }

    private void setColors(int color){
        int[] siteColors = getResources().getIntArray(R.array.siteColors);
        tabLayout.setBackgroundColor(siteColors[color]);
        toolbar.setBackgroundColor(siteColors[color]);
        getWindow().setStatusBarColor(Utility.darker(siteColors[color],0.8f));
    }

}
