package com.heapdragon.lots;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mSitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE);
    private RecyclerView mSitesRecyclerView;
    private SiteAdapter mSiteAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Site> sites = new ArrayList<>();
    private ProgressBar pb;
    private TextView noSitesTextView;
    private View connectionFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        pb = (ProgressBar) findViewById(R.id.main_activity_progressBar);
        noSitesTextView = (TextView) findViewById(R.id.activity_main_textview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSitesRecyclerView = (RecyclerView) findViewById(R.id.site_activity_sites_recycler_view);
        connectionFrag = findViewById(R.id.connection_frag);
        mSitesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSiteAdapter = new SiteAdapter(sites);
        mSitesRecyclerView.setAdapter(mSiteAdapter);
        tryLoadingSites();
        connectionFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLoadingSites();
            }
        });



    }

    private Site createSiteFromNode(DataSnapshot ds){
        String name = ds.child(NAME_NODE).getValue().toString();
        int numberOfLots = Integer.valueOf(ds.child(TOTAL_LOTS_NODE).getValue().toString());
        int incompleteLots = Integer.valueOf(ds.child(INCOMPLETE_LOTS_NODE).getValue().toString());
        int issueLots = Integer.valueOf(ds.child(ISSUE_LOTS_NODE).getValue().toString());
        int readyLots = Integer.valueOf(ds.child(READY_LOTS_NODE).getValue().toString());
        int receivedLots = Integer.valueOf(ds.child(RECEIVED_LOTS_NODE).getValue().toString());
        int siteColor = Integer.valueOf(ds.child(SITE_COLOR_NODE).getValue().toString());
        String id = ds.getKey();
        return new Site(name,numberOfLots,incompleteLots,issueLots,readyLots,receivedLots,siteColor,id);
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
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void tryLoadingSites(){
        if(checkInternet()){
            connectionFrag.setVisibility(View.GONE);
            //LISTENS TO FOR CHANGES IN THE SITE NODE
            mSitesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sites.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        try{
                            MainActivity.this.sites.add(createSiteFromNode(ds));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    pb.setVisibility(View.GONE);
                    MainActivity.this.mSiteAdapter.notifyDataSetChanged();
                    if(sites.size()<1){
                        noSitesTextView.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");
    }
}
