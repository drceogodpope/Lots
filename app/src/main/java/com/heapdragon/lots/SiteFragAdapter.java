package com.heapdragon.lots;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SiteFragAdapter extends FragmentPagerAdapter {

    private String key;

    public SiteFragAdapter(FragmentManager fm,String key) {
        super(fm);
        this.key = key;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return LotsFragment.newInstance(key);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SiteMapFragment.newInstance(key);
            case 2: // Fragment # 1 - This will show SecondFragment
                return SiteLogFragment.newInstance(key);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Lots";
            case 1:return "Map";
            case 2:return "Log";
        }
        return "";
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
