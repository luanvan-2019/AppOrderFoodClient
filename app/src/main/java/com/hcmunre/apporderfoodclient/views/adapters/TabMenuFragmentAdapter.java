package com.hcmunre.apporderfoodclient.views.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodclient.views.fragments.AllRestaurant;
import com.hcmunre.apporderfoodclient.views.fragments.Drinks;
import com.hcmunre.apporderfoodclient.views.fragments.Eats;
import com.hcmunre.apporderfoodclient.views.fragments.Vegetarians;

public class TabMenuFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumofTabs;
    public TabMenuFragmentAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mNumofTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllRestaurant tab1 = new AllRestaurant();
                return tab1;

            case 1:
                Eats tab2 = new Eats();
                return tab2;

            case 2:
                Vegetarians tab3 = new Vegetarians();
                return tab3;

            case 3:
                Drinks tab4 = new Drinks();
                return tab4;

            default:
                return null;
        }
    }
        @Override
        public int getCount()

        { return mNumofTabs;}

}
