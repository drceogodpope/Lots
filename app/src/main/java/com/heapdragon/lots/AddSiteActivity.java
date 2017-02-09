package com.heapdragon.lots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.heapdragon.lots.AddSiteActivity.*;
import static com.heapdragon.lots.DataBaseConstants.INCOMPLETE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.ISSUE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.RECEIVED_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_COLOR_NODE;
import static com.heapdragon.lots.DataBaseConstants.TOTAL_LOTS_NODE;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddSiteActivity extends AppCompatActivity {

    private static final String TAG = "AddSiteActivityTAG";

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private EditText siteName;
    private EditText numberOfLots;
    private Button createSiteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        android.util.Log.d(TAG,"onCreate()");

        siteName = (EditText) findViewById(R.id.add_site_activity_name);
        numberOfLots = (EditText) findViewById(R.id.add_site_activity_total_lots);
        createSiteButton = (Button) findViewById(R.id.add_site_activity_create_site_button);

        createSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSite();
                startMainActivity();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void createSite() {
            Log.d(TAG,"createSite()");
            if(numberOfLots.getText().toString().length()>0&&siteName.getText().toString().length()>0){
                String name = Utility.capitilizeFirst(siteName.getText().toString());
                int numLots = Integer.parseInt(numberOfLots.getText().toString());
                Site site = new Site(name,numLots);
                try {
                    String key = createSiteNode(site);
                    createLotNode(key,site.getNumberOfLots());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Check number of lots!",Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(1000).playOn(numberOfLots);
                    Log.d("", numLots + " is not a number");
                }
            }

    }


    private String createSiteNode(Site site){
        DatabaseReference sitesRef = mRootRef.child(SITES_NODE).getRef();
        String key;
        Map<String,Object> map = new HashMap<>();
        map.put(NAME_NODE,site.getName());
        map.put(TOTAL_LOTS_NODE,site.getNumberOfLots());
        map.put(INCOMPLETE_LOTS_NODE,site.getIncompleteLots());
        map.put(RECEIVED_LOTS_NODE,site.getReceivedLots());
        map.put(READY_LOTS_NODE,site.getReadyLots());
        map.put(ISSUE_LOTS_NODE,site.getIssue_lots());
        map.put(SITE_COLOR_NODE,site.getSiteColor());

        key = sitesRef.push().getKey();
        sitesRef.child(key).setValue(map);
        return key;
    }

    private void createLotNode(String key,int numLots) {
        DatabaseReference lotRef = mRootRef.child(LOTS_NODE_PREFIX+key).getRef();
        for(int i = 1; i<=numLots;i++){
            lotRef.child(String.valueOf(i)).setValue(0);
        }
    }

    private void startMainActivity(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }


}
