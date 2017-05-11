package com.heapdragon.lots;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Francesco on 2017-05-11.
 */

public class SiteLogFrag extends LogFrag {

    public static LogFrag newInstance(String key) {
        Bundle args = new Bundle();
        args.putString("key", key);
        LogFrag fragment = new SiteLogFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    boolean checkField(DataSnapshot ds) {
        return true;
    }
}
