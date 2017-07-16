package com.heapdragon.lots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public abstract class ScrollableParentFrag extends Fragment{

    protected String key;
    protected int lotNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
        lotNumber = getArguments().getInt("lotNumber");
    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState==null){
            addChildFrags(createFrags());
        }
    }



    //IN THIS METHOD YOU MUST CREATE A NEW ArrayList<Fragment> FILL IT WITH NEW INSTANCES OF FRAGMENTS THAT
    // YOU WANT IN THIS ScrollableView SUBCLASS
    // AND THEN RETURN THE ArrayList<FRAGMENT>
    // addChildFrags() WILL THEN LOOP THROUGH THIS ArrayList<Fragment> and add them to THIS ScrollableView SUBCLASS
    // sequentially.
    protected abstract ArrayList<Fragment> createFrags();


    protected abstract void addChildFrags(ArrayList<Fragment> fragments);


}
