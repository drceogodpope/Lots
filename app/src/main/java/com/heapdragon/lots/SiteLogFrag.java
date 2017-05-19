package com.heapdragon.lots;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;

import java.util.ArrayList;

import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;

public class SiteLogFrag extends LogFrag {

    public static LogFrag newInstance(String key) {
        Bundle args = new Bundle();
        args.putString("key", key);
        LogFrag fragment = new SiteLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void queryLogs(DataSnapshot dataSnapshot, ArrayList<SiteLog> logs) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            long lotNumber = (long) ds.child(LOG_NUMBER).getValue();
            long priority = (long)ds.child(LOG_FIELD_UPDATED).getValue();
            String logKey = ds.getKey();
            DateTime dateTime = new DateTime(ds.child(LOG_TIME_STAMP).getValue());
            long status = (long) ds.child(LOG_STATUS).getValue();
            logs.add(new SiteLog(dateTime, lotNumber, (int)status,logKey,key,priority));
        }
    }
}
