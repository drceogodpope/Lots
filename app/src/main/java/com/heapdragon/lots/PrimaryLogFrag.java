package com.heapdragon.lots;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Francesco on 2017-05-11.
 */

public class PrimaryLogFrag extends LogFrag {

    public static LogFrag newInstance(String key,int lotNumber) {
        Bundle args = new Bundle();
        args.putString("key", key);
        PrimaryLogFrag fragment = new PrimaryLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    boolean checkField(DataSnapshot ds) {
        return (ds.child(DataBaseConstants.LOG_FIELD_UPDATED).getValue()).equals(DataBaseConstants.LOTS_PRIMARY_STATUS_PREFIX);
    }
}
