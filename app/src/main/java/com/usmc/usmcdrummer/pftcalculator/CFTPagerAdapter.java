package com.usmc.usmcdrummer.pftcalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CFTPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CFTPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                cftfrag tab1 = new cftfrag();
                return tab1;
            case 1:
                cftwhatiffrag tab2 = new cftwhatiffrag();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}