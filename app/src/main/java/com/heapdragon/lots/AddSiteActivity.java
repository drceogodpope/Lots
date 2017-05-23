package com.heapdragon.lots;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.concurrent.ThreadLocalRandom;

public class AddSiteActivity extends AppCompatActivity implements ColorChooserFrag.OnColorChosenListener{

    private static final String TAG = "AddSiteActivityTAG";
    private static final int GALLERY_INTENT = 2;

    private EditText siteName;
    private EditText numberOfLotsN;
    private EditText numberOfLotsM;
    private CardView siteColorPicker;
    private Button createSiteButton;
    private ImageButton chooseSiteMapButton;
    private Uri siteMapUri;
    private FrameLayout fl;
    private int siteColor;
    private int[] siteColors;

    private MapUploaderBitch dbBitch;

    private class MapUploaderBitch extends DatabaseBitch{
        @Override
        protected void next() {
            super.next();
            startActivity(new Intent(AddSiteActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        //VIEWS//
        siteName = (EditText) findViewById(R.id.add_site_activity_name);
        numberOfLotsN = (EditText) findViewById(R.id.n_lots);
        numberOfLotsM = (EditText) findViewById(R.id.m_lots);
        createSiteButton = (Button) findViewById(R.id.create_button);
        siteColorPicker = (CardView) findViewById(R.id.colorPicker);
        chooseSiteMapButton = (ImageButton) findViewById(R.id.choose_site_map_button);
        fl = (FrameLayout) findViewById(R.id.colorFrag_layout);
        fl.setVisibility(View.GONE);
        //VIEWS//


        //INIT OBJECTS//
        dbBitch = new MapUploaderBitch();
        siteColors = siteName.getResources().getIntArray(R.array.siteColors);
        siteColor = ThreadLocalRandom.current().nextInt(0,siteColors.length);
        siteColorPicker.setBackgroundColor(siteColors[siteColor]);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //INIT OBJECTS//

        //CREATE DYNAMIC VIEWS//
        addColorChooserFrag();
        //CREATE DYNAMIC VIEWS//


        //ADD ONCLICK LISTENERS//
        createSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSiteName()&&checkLotRange()){
                    String name = Utility.capitilizeFirst(siteName.getText().toString());
                    int n = Integer.valueOf(numberOfLotsN.getText().toString());
                    int m =  Integer.valueOf(numberOfLotsM.getText().toString());
                    Site site = new Site(name,n,m);

                    String siteKey = dbBitch.createSiteNode(site,siteColor);
                    dbBitch.createLotNode(siteKey,n,m);
                    dbBitch.setSiteMap(siteKey,AddSiteActivity.this,siteMapUri);
                }
            }
        });
        siteColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fl.getVisibility()==View.GONE)showColorPickerFrag();
                else hideColorPickerFrag();
            }
        });
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
        //ADD ONCLICK LISTENERS//
    }


    private boolean checkSiteName(){
        if(siteName.getText().toString().trim().length()>0){
            return true;
        }
        else {
            YoYo.with(Techniques.Shake).duration(500).playOn(siteName);
            Toast.makeText(getApplicationContext(), "Enter name of the site!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean checkLotRange(){
        int nLots = Integer.parseInt(numberOfLotsN.getText().toString());
        int mLots = Integer.parseInt(numberOfLotsM.getText().toString());
        if(mLots>nLots && mLots-nLots+1<=2000 && nLots>0 &mLots<9999){
                return true;
        }
        else {
            YoYo.with(Techniques.Shake).duration(500).playOn(numberOfLotsN);
            YoYo.with(Techniques.Shake).duration(500).playOn(numberOfLotsM);
            Toast.makeText(getApplicationContext(),
                    "Enter a valid Range. Total number of lots in range must not exceed 2000!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private void addColorChooserFrag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_in)
                .add(R.id.colorFrag_layout,ColorChooserFrag.newInstance())
                .commit();
    }

    //COLOR PICKER METHODS
    @Override
    public void onColorChosen(int color) {
        siteColor = color;
        siteColorPicker.setBackgroundColor(siteColors[color]);
        fl.setVisibility(View.GONE);
    }

    private void showColorPickerFrag() {
        fl.setVisibility(View.VISIBLE);
    }

    private void hideColorPickerFrag() {
        fl.setVisibility(View.GONE);
    }
    private int assignSiteColor(){
        return ThreadLocalRandom.current().nextInt(0,siteColors.length);
    }
    //COLOR PICKER METHODS


    //SITE MAP METHODS//
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
    //SITE MAP METHODS//


}
