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

public class PrimaryLogFrag extends LogFrag implements Deactivatable {
    private static final String TAG = "PrimaryLogFrag";

    public static PrimaryLogFrag newInstance(String key,int lotNumber) {
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("lotNumber",lotNumber);
        PrimaryLogFrag fragment = new PrimaryLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void queryLogs(DataSnapshot dataSnapshot, ArrayList<SiteLog> logs) {
        Log.d(TAG,"queryLogs()");

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
            String logKey = ds.getKey();
            String value = ds.child(LOG_STATUS).getValue().toString();
            String fieldUpdated = ds.child(LOG_FIELD_UPDATED).getValue().toString();
            DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
            logs.add(new SiteLog(dateTime, lotNumber,value,logKey,key,fieldUpdated));
        }

    }

}
