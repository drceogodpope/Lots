package com.heapdragon.lots;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;

import java.util.ArrayList;

import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;
import static com.heapdragon.lots.DataBaseConstants.LOTS_ARCH_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOTS_MATERIAL_ORDERED;

public class MaterialLogFrag extends LogFrag implements Deactivatable {
    private static final String TAG = "MaterialLogFrag";

    public static MaterialLogFrag newInstance(String key, int lotNumber) {
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("lotNumber", lotNumber);
        MaterialLogFrag fragment = new MaterialLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void queryLogs(DataSnapshot dataSnapshot, ArrayList<SiteLog> logs) {
        Log.d(TAG, "queryLogs()");
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String fieldUpdated = ds.child(LOG_FIELD_UPDATED).getValue().toString();
            long number = (long) ds.child(LOG_NUMBER).getValue();
            Log.d(TAG, "fieldUpdated = " + fieldUpdated +
                    "\nnumber = " + number);
            if (fieldUpdated.equals(LOTS_MATERIAL_ORDERED) && number == lotNumber) {
                String logKey = ds.getKey();
                String value = ds.child(LOG_STATUS).getValue().toString();
                DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
                logs.add(new SiteLog(dateTime, number, value, logKey, key, fieldUpdated));
            } else {
                Log.d(TAG, fieldUpdated + " : " + LOTS_MATERIAL_ORDERED);
                Log.d(TAG, number + " :  " + lotNumber);
            }
        }
    }
}