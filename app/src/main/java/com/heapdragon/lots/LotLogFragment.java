package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class LotLogFragment extends SiteLogFragment {

    private static String TAG = "LotLogFragment";
     public static LotLogFragment newInstance(String key,int lotNumber) {
         Bundle args = new Bundle();
         args.putInt("lotNumber",lotNumber);
         args.putString("key",key);
         LotLogFragment fragment = new LotLogFragment();
         fragment.setArguments(args);
         return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lotNumber = getArguments().getInt("lotNumber");
        Log.d(TAG,String.valueOf(lotNumber));
    }
}
