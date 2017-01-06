package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SiteMapFragment extends android.support.v4.app.Fragment {
    private LinearLayout linearLayout;
    private ImageButton addSiteMapButton;

    public static SiteMapFragment newInstance(String key){
        SiteMapFragment siteMapFragment = new SiteMapFragment();
        Bundle args = new Bundle();
        args.putString("key",key);
        siteMapFragment.setArguments(args);
        return siteMapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        linearLayout = (LinearLayout) view.findViewById(R.id.add_site_map_layout);
        linearLayout.setVisibility(View.VISIBLE);
        return view;
    }
}
