package com.heapdragon.lots;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import static com.heapdragon.lots.DataBaseConstants.*;

public class SitesActivity extends AppCompatActivity {
    private static final String TAG = "SitesActivity";
    private SiteAdapter2 mSiteAdapter;
    private LinearLayout noSitesLayout;
    private ProgressBar pb;
    private View connectionFrag;
    private RecyclerView mSitesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseBitch databaseBitch;
    private ArrayList<Site> sites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //FIND VIEWS//
        pb = (ProgressBar) findViewById(R.id.main_activity_progressBar);
        noSitesLayout = (LinearLayout) findViewById(R.id.no_site_layout);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mSitesRecyclerView = (RecyclerView) findViewById(R.id.site_activity_sites_recycler_view);
        connectionFrag = findViewById(R.id.connection_frag);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SitesActivity_swipe_refresh);
        //FIND VIEWS//

        //INITIALIZE OBJECTS//
        databaseBitch = new DatabaseBitch();
        sites = new ArrayList<>();
        mSiteAdapter = new SiteAdapter2(sites);
        //INITIALIZE OBJECTS//


        mSitesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSiteAdapter = new SiteAdapter2(sites);
        mSitesRecyclerView.setAdapter(mSiteAdapter);
        tryLoadingSites();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseBitch.getSites(sites,mSiteAdapter,pb,noSitesLayout,mSitesRecyclerView);
                resetRefresher();
            }
        });

        connectionFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLoadingSites();
            }
        });
        noSitesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SitesActivity.this,AddSiteActivity.class));
            }
        });
    }

    void resetRefresher(){
        swipeRefreshLayout.setRefreshing(false);
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
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,LoginActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPlayService(){
        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // Check Google Play Service Available
        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, this, 1).show();
            }else{
                databaseBitch.getSites(sites,mSiteAdapter,pb,noSitesLayout,mSitesRecyclerView);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void tryLoadingSites(){
        if(checkInternet()){
            connectionFrag.setVisibility(View.GONE);
            checkPlayService();
        }
        else{
            pb.setVisibility(View.GONE);
            connectionFrag.setVisibility(View.VISIBLE);
        }
    }

    boolean checkInternet(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
