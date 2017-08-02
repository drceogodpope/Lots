package com.heapdragon.lots;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SecondaryScrollableFrag extends ScrollableParentFrag {

    public static SecondaryScrollableFrag newInstance(Bundle args){
        SecondaryScrollableFrag fragment = new SecondaryScrollableFrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scrollable2, container, false);
    }

    @Override
    protected void addChildFrags(ArrayList<Fragment> fragments){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment frag:fragments){
            transaction.add(R.id.primary_status_root2,frag);
        }
        transaction.commit();
    }

    @Override
    protected ArrayList<Fragment> createFrags() {
        ArrayList<Fragment> frags = new ArrayList<>();
        ArchStatusFrag archStatusFrag = ArchStatusFrag.newInstance(key,lotNumber);
        ArchDotFragment archDotFragment =  ArchDotFragment.newInstance(key,lotNumber);
        ArchLotFragment archLotFragment = ArchLotFragment.newInstance(key,lotNumber);
        PrimaryLogFrag logFrag = PrimaryLogFrag.newInstance(key,lotNumber);

        ((Activator) getActivity()).addFrag(archStatusFrag);
        ((Activator) getActivity()).addFrag(archDotFragment);
        ((Activator) getActivity()).addFrag(logFrag);

        frags.add(archDotFragment);
        frags.add(archStatusFrag);
        frags.add(archLotFragment);
        frags.add(logFrag);
        return frags;
    }

}
