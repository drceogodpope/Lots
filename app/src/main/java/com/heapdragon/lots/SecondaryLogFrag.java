package com.heapdragon.lots;
import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;

public class SecondaryLogFrag extends LogFrag {

    public static LogFrag newInstance(String key,int lotNumber) {
        Bundle args = new Bundle();
        args.putString("key", key);
        SecondaryLogFrag fragment = new SecondaryLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    boolean checkField(DataSnapshot ds) {
        return (ds.child(DataBaseConstants.LOG_FIELD_UPDATED).getValue()).equals(DataBaseConstants.LOTS_SECONDARY_STATUS_PREFIX);
    }
}
