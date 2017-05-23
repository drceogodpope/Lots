package com.heapdragon.lots;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.DateTime;

import java.net.URI;
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
import static com.heapdragon.lots.DataBaseConstants.LOTS_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOTS_SECONDARY_STATUS_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.NAME_NODE;
import static com.heapdragon.lots.DataBaseConstants.READY_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.RECEIVED_LOTS_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITES_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_COLOR_NODE;
import static com.heapdragon.lots.DataBaseConstants.SITE_MAPS_ROOT;
import static com.heapdragon.lots.DataBaseConstants.SITE_M_LOT;
import static com.heapdragon.lots.DataBaseConstants.SITE_N_LOT;
import static com.heapdragon.lots.DataBaseConstants.TOTAL_LOTS_NODE;
import static java.security.AccessController.getContext;

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

     void createLotNode(String siteKey,int n,int m) {
        DatabaseReference lotRef = FirebaseDatabase.getInstance().getReference().child(LOTS_NODE_PREFIX+siteKey).getRef();
        for(int i=n; i<=m;i++){
            lotRef.child(String.valueOf(i)).child(LOTS_PRIMARY_STATUS_PREFIX).setValue(0);
            lotRef.child(String.valueOf(i)).child(LOTS_SECONDARY_STATUS_PREFIX).setValue(0);
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
