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

import java.util.ArrayList;

public class PrimaryScrollableFrag extends ScrollableParentFrag {

    public static PrimaryScrollableFrag newInstance(Bundle args){
        PrimaryScrollableFrag fragment = new PrimaryScrollableFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scrollable, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void addChildFrags(ArrayList<Fragment> fragments){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment frag:fragments){
            transaction.add(R.id.primary_status_root,frag);
        }
        transaction.commit();
    }


    @Override
    protected ArrayList<Fragment> createFrags() {
        ArrayList<Fragment> frags = new ArrayList<>();
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("lotNumber", lotNumber);
        LotPrimaryStatusFragment primaryStatusFrag = new LotPrimaryStatusFragment();
        primaryStatusFrag.setArguments(args);
        Fragment lotLogFrag = PrimaryLogFrag.newInstance(key,lotNumber);
        frags.add(primaryStatusFrag);
        frags.add(lotLogFrag);
        return frags;
    }




}
