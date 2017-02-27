package com.heapdragon.lots;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.heapdragon.lots.DataBaseConstants.INCOMPLETE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.ISSUE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.RECEIVED_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_COLOR_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;
import static com.heapdragon.lots.DataBaseConstants.TOTAL_LOTS_NODE;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AddSiteActivity extends AppCompatActivity {

    private static final String TAG = "AddSiteActivityTAG";

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private EditText siteName;
    private EditText numberOfLots;
    private CardView siteColorPicker;
    private Button createSiteButton;
    private ImageButton chooseSiteMapButton;
    private Uri siteMapUri;
    private StorageReference  mStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private FrameLayout fl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        android.util.Log.d(TAG,"onCreate()");

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
        mStorage = FirebaseStorage.getInstance().getReference();
        fl = (FrameLayout) findViewById(R.id.colorFrag_layout);
        fl.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = View.generateViewId();
        Log.d(TAG,"ID: "+String.valueOf(id));
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_in)
                .add(R.id.colorFrag_layout,ColorChooserFrag.newInstance())
                .commit();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Creating Site!");

        siteName = (EditText) findViewById(R.id.add_site_activity_name);
        numberOfLots = (EditText) findViewById(R.id.add_site_activity_total_lots);
        createSiteButton = (Button) findViewById(R.id.create_button);
        siteColorPicker = (CardView) findViewById(R.id.colorPicker);
        chooseSiteMapButton = (ImageButton) findViewById(R.id.choose_site_map_button);

        siteColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fl.getVisibility()==View.GONE)showColorPickerFrag();
                else hideColorPickerFrag();
            }
        });

        createSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               createSite();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        chooseSiteMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(siteMapUri==null){
                    chooseSiteMap();
                }
                else {
                    siteMapUri=null;
                    Toast.makeText(AddSiteActivity.this, "Site map un-chosen!", Toast.LENGTH_SHORT).show();
                    chooseSiteMapButton.setImageResource(R.drawable.photo_library_grey_96x96);
                }
            }
        });

    }


    private void chooseSiteMap() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){
             Toast.makeText(this, "Site map chosen!", Toast.LENGTH_SHORT).show();
             siteMapUri = data.getData();
             chooseSiteMapButton.setImageResource(R.drawable.insert_photo_white_96x96);
         }
    }

    private void showColorPickerFrag() {
        fl.setVisibility(View.VISIBLE);
    }

    private void hideColorPickerFrag() {
        fl.setVisibility(View.GONE);
    }

    private boolean createSite() {
        Log.d(TAG,"createSite()");
            if(siteName.getText().toString().trim().length()>0){
                if(numberOfLots.getText().toString().trim().length()>0){
                    String name = Utility.capitilizeFirst(siteName.getText().toString());
                    int numLots = Integer.parseInt(numberOfLots.getText().toString());
                    Site site = new Site(name,numLots);
                    if(numLots<=2000&&numLots>0){
                        try {
                            String key = createSiteNode(site);
                            createLotNode(key,site.getNumberOfLots());
                            setSiteMap(key);
                            return true;
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(),"Check number of lots!",Toast.LENGTH_SHORT).show();
                            YoYo.with(Techniques.Shake).duration(500).playOn(numberOfLots);
                            Log.d("", numLots + " is not a number");
                            return false;
                        }
                    }
                    else{
                        YoYo.with(Techniques.Shake).duration(500).playOn(numberOfLots);
                        Toast.makeText(getApplicationContext(),
                                "Enter number of lots. Must be between 1 and 2000!",
                                Toast.LENGTH_LONG).show();
                    }

                }else {
                    YoYo.with(Techniques.Shake).duration(500).playOn(numberOfLots);
                    Toast.makeText(getApplicationContext(),
                            "Enter number of lots. Must be between 1 and 1000!",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                YoYo.with(Techniques.Shake).duration(500).playOn(siteName);
                Toast.makeText(getApplicationContext(),
                        "Enter name of the site!",
                        Toast.LENGTH_LONG).show();
            }
        return false;
    }

    private boolean setSiteMap(String key) {
        mProgressDialog.show();
        StorageReference filePath = mStorage.child(SITE_MAPS_ROOT).child(key);
        try{
            filePath.putFile(siteMapUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getBaseContext(),"Site map uploaded!",Toast.LENGTH_SHORT).show();
                    startMainActivity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,e.toString());
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Error uploading site map!",Toast.LENGTH_LONG).show();
                    startMainActivity();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            startMainActivity();
            return false;
        }
        return true;
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
        map.put(SITE_COLOR_NODE,assignSiteColor());

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

    private int assignSiteColor(){
        int[] siteColors = siteName.getResources().getIntArray(R.array.siteColors);
        return ThreadLocalRandom.current().nextInt(0,siteColors.length);
    }

}
