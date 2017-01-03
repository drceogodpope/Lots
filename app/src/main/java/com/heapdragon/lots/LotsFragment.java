package com.heapdragon.lots;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LotsFragment extends android.support.v4.app.Fragment {
    private final static String TAG = "LotsFragmentTag";
    private RecyclerView recyclerView;
    private TextView textView;
    private String key;
    private String title;
    private int page;

    public static LotsFragment newInstance(String key){
        LotsFragment lotsFragment = new LotsFragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        lotsFragment.setArguments(args);
        return lotsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page_number",0);
        title = getArguments().getString("page_title");
        key = getArguments().getString("key");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lots,container,false);
        textView = (TextView) view.findViewById(R.id.lots_fragment_textView);
        textView.setText(key);
        return view;
    }
}
