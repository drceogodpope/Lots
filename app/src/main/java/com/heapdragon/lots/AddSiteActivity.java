package com.heapdragon.lots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import static com.heapdragon.lots.AddSiteActivity.*;
import static com.heapdragon.lots.DataBaseConstants.INCOMPLETE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.ISSUE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.RECEIVED_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.TOTAL_LOTS_NODE;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francesco on 2016-12-26.
 */

public class AddSiteActivity extends AppCompatActivity {

    private static final String TAG = "AddSiteActivity";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lots-676e3.firebaseio.com/");


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
    }

    private void createSite() {

        String name = siteName.getText().toString();
        String numLots = numberOfLots.getText().toString();
        try {
            String key = createSiteNode(name,numLots);
            createLotNode(key,Integer.parseInt(numLots));
        } catch (NumberFormatException e) {
            Log.d("", numLots + " is not a number");
        }


    }

    private String createSiteNode(String name,String numLots){
        DatabaseReference sitesRef = mRootRef.child(SITES_NODE).getRef();
        String key;
        int num = Integer.parseInt(numLots);
        android.util.Log.d("",num+" is a number");
        Map<String,String> map = new HashMap<>();
        map.put(NAME_NODE,name);
        map.put(TOTAL_LOTS_NODE,numLots);
        map.put(INCOMPLETE_LOTS_NODE,numLots);
        map.put(RECEIVED_LOTS_NODE,"0");
        map.put(READY_LOTS_NODE,"0");
        map.put(ISSUE_LOTS_NODE,"0");

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
