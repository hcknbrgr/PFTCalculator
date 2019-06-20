package com.usmc.usmcdrummer.pftcalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BFPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public BFPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                heightweight_frag tab1 = new heightweight_frag();
                return tab1;
            case 1:
                bodyfatcalc_frag tab2 = new bodyfatcalc_frag();
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