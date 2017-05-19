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

public class PrimaryLogFrag extends LogFrag {
    private static final String TAG = "PrimaryLogFrag";

    public static LogFrag newInstance(String key,int lotNumber) {
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
            long priority = (long)ds.child(LOG_FIELD_UPDATED).getValue();

            if(lotNumber == getArguments().getInt("lotNumber") && priority == SiteLog.PRIMARY){
                String logKey = ds.getKey();
                DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
                long status = (long) ds.child(LOG_STATUS).getValue();
                logs.add(new SiteLog(dateTime, lotNumber, (int)status,logKey,key,priority));
            }
        }
    }
}
