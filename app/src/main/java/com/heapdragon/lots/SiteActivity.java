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

import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;

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

        tabLayout = (TabLayout) findViewById(R.id.site_activity_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
    }

//    public class SectionsPagerAdapter extends PagerAdapter {
//        private Context mContext;
//        String key;
//
//        public SectionsPagerAdapter(Context context,String key) {
//            mContext = context;
//            this.key = key;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ModelObject modelObject = ModelObject.values()[position];
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), container, false);
//            container.addView(layout);
//            return layout;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//
//        @Override
//        public int getCount() {
//            return ModelObject.values().length;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            ModelObject customPagerEnum = ModelObject.values()[position];
//            return mContext.getString(customPagerEnum.getTitleResId());
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
