package com.heapdragon.lots;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
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

}
