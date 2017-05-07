package com.heapdragon.lots;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Francesco on 2017-05-02.
 */

class LotFragAdapter extends FragmentPagerAdapter {

    private  String key;
    private  int lotNumber;

    public LotFragAdapter(FragmentManager fm,String key,int lotNumber) {
        super(fm);
        this.lotNumber = lotNumber;
        this.key = key;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("key",key);
        bundle.putInt("lotNumber",lotNumber);

        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return PrimaryScrollableFrag.newInstance(bundle);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SecondaryScrollableFrag.newInstance(bundle);
            case 2: // Fragment # 1 - This will show SecondFragment
                return LotLogFragment.newInstance(key);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Status 1";
            case 1:return "Status 2";
            default:return "Issue";
        }
    }


    @Override
    public int getCount() {
        return 3;
    }
}


