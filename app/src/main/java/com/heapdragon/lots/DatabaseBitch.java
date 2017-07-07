package com.heapdragon.lots;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.heapdragon.lots.DataBaseConstants.INCOMPLETE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.ISSUE_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_ARCH_ORDERED;
import static com.heapdragon.lots.DataBaseConstants.LOTS_ARCH_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_MATERIAL_ORDERED;
import static com.heapdragon.lots.DataBaseConstants.LOTS_ARCH_LOT;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.RANGES;
import static com.heapdragon.lots.DataBaseConstants.RANGES_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.RECEIVED_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_COLOR_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;
import static com.heapdragon.lots.DataBaseConstants.SITE_M_LOT;
import static com.heapdragon.lots.DataBaseConstants.SITE_N_LOT;
import static com.heapdragon.lots.DataBaseConstants.TOTAL_LOTS_NODE;

class DatabaseBitch {

    private static final String TAG = "DatabaseBitch";
    void createLog(int status, String siteKey,int lotNumber,long priorityLevel) {
         DatabaseReference logRef = FirebaseDatabase.getInstance().getReference().child(LOG_NODE_PREFIX+siteKey);
         Map<String,Object> map = new HashMap<>();
         map.put(LOG_NUMBER,lotNumber);
         map.put(LOG_FIELD_UPDATED,priorityLevel);
         map.put(LOG_STATUS,status);
         map.put(LOG_TIME_STAMP,new DateTime().getMillis());
         logRef.push().setValue(map);
    }

     ArrayList<SiteLog> getAllLogs(final ArrayList<SiteLog> logs, final String key) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference logRef = rootRef.child(LOG_NODE_PREFIX + key).getRef();
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logs.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String logKey = ds.getKey();
                    long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
                    DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
                    long status = (long) ds.child(LOG_STATUS).getValue();
                    long priority = (long)ds.child(LOG_FIELD_UPDATED).getValue();
                    logs.add(new SiteLog(dateTime, lotNumber, (int) status,logKey,key,priority));
                }
                Collections.sort(logs, new Comparator<SiteLog>() {
                    @Override
                    public int compare(SiteLog siteLog, SiteLog t1) {
                        return t1.getDateTime().compareTo(siteLog.getDateTime());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               Log.d(TAG,databaseError.toString());
            }
        });
        return logs;
    }

     String createSiteNode(Site site,int siteColor){
        DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE).getRef();
        String key;
        Map<String,Object> map = new HashMap<>();
         map.put(NAME_NODE,site.getName());
         map.put(TOTAL_LOTS_NODE,site.getNumberOfLots());
         map.put(SITE_COLOR_NODE,siteColor);
         map.put(SITE_N_LOT,site.getN());
         map.put(SITE_M_LOT,site.getM());
         map.put(INCOMPLETE_LOTS_NODE,site.getIncompleteLots());
         map.put(RECEIVED_LOTS_NODE,site.getReceivedLots());
         map.put(READY_LOTS_NODE,site.getReadyLots());
         map.put(ISSUE_LOTS_NODE,site.getIssue_lots());

        key = sitesRef.push().getKey();
        sitesRef.child(key).setValue(map);
        return key;
    }

    String createSiteNode2(Site site,int siteColor){
        DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE).getRef();
        String key;
        Map<String,Object> map = new HashMap<>();
        map.put(NAME_NODE,site.getName());
        map.put(RANGES,site.getLotIntervals());
        map.put(SITE_COLOR_NODE,siteColor);
        key = sitesRef.push().getKey();
        sitesRef.child(key).setValue(map);
        return key;
    }

    void getSites(final ArrayList<Site> sites, final SiteAdapter2 adapter, final ProgressBar pb, final View noFragsView, final RecyclerView rv){
        DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE).getRef();
        sites.clear();
        sitesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                pb.setVisibility(View.GONE);

                if(dataSnapshot.getChildrenCount()<1){
                    noFragsView.setVisibility(View.VISIBLE);

                }
                else {
                    rv.setVisibility(View.VISIBLE);
                }
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String name = ds.child(NAME_NODE).getValue().toString();
                    int siteColor = Integer.valueOf(ds.child(SITE_COLOR_NODE).getValue().toString());
                    String key = ds.getKey();
                    Log.d(TAG,name);
                    ArrayList<LotInterval> lotIntervals = new ArrayList<>();
                    for(DataSnapshot ds1:ds.child(RANGES).getChildren()){
                        long n = (long)ds1.child("n").getValue();
                        long m = (long)ds1.child("m").getValue();
                        LotInterval lotInterval = new LotInterval(n,m);
                        Log.d(TAG,lotInterval.toString());
                        lotIntervals.add(new LotInterval(n,m));
                    }
                    sites.add(new Site(name,lotIntervals,siteColor,key));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        Log.d(TAG,"sites.size() = "+String.valueOf(sites.size()));
    }

    void createIntervalsNode(ArrayList<LotInterval> lotIntervals,String key){
        DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child(RANGES_PREFIX+key).getRef();
        for(int i = 0; i<lotIntervals.size();i++){
            sitesRef.child(String.valueOf(i)).setValue(lotIntervals.get(i));
        }
    }

    Site getSite(String key){
        DatabaseReference siteRef = FirebaseDatabase.getInstance().getReference().child(SITES_NODE).child(key).getRef();
        final ArrayList<LotInterval> lotIntervals = new ArrayList<>();
        final String[] name = new String[1];
        final int[] siteColor = new int[1];
        final String[] id = new String[1];
        siteRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name[0] = dataSnapshot.child(NAME_NODE).getValue().toString();
                siteColor[0] = Integer.valueOf(dataSnapshot.child(SITE_COLOR_NODE).getValue().toString());
                id[0] = dataSnapshot.getKey();
                for(DataSnapshot ds1:dataSnapshot.child(RANGES).getChildren()){
                    long n = (long)ds1.child("n").getValue();
                    long m = (long)ds1.child("m").getValue();
                    lotIntervals.add(new LotInterval(n,m));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return new Site(name[0],lotIntervals,siteColor[0],id[0]);
    }

     void createLotNode(String siteKey,int n,int m) {
        DatabaseReference lotRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+siteKey).getRef();
        for(int i=n;i<=m;i++){
            lotRef.child(String.valueOf(i)).child(LOTS_MATERIAL_ORDERED).setValue(0);
            lotRef.child(String.valueOf(i)).child(LOTS_ARCH_LOT).setValue(0);
        }
    }

    void createLotNode2(String siteKey,ArrayList<LotInterval> lotIntervals) {
        DatabaseReference lotRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+siteKey).getRef();
        for(LotInterval lotInterval:lotIntervals){
            long m = lotInterval.getM();
            for(long n = lotInterval.getN();n<=m;n++){
                lotRef.child(String.valueOf(n)).child(LOTS_ARCH_LOT).setValue(false);
                lotRef.child(String.valueOf(n)).child(LOTS_ARCH_ORDERED).setValue(false);
                lotRef.child(String.valueOf(n)).child(LOTS_ARCH_STATUS).setValue(Lot.WORK_ORDER_REQUIRED);
                lotRef.child(String.valueOf(n)).child(LOTS_MATERIAL_ORDERED).setValue(false);
            }

        }
    }

      void setSiteMap(String key,final Context context, Uri siteMapUri) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Creating Site!");
            progressDialog.show();
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child(SITE_MAPS_ROOT).child(key);
            try{
                filePath.putFile(siteMapUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Site map uploaded!",Toast.LENGTH_SHORT).show();
                        next();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Error UploadingSiteMap",Toast.LENGTH_SHORT).show();
                        next();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                next();
            }
      }

    protected void next(){}

}
