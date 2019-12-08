package com.hcmunre.apporderfoodclient.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodclient.views.fragments.MenuListFragment;
import com.hcmunre.apporderfoodclient.views.fragments.ReviewFragment;

public class TabMenuFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumofTab;

    public TabMenuFragmentAdapter(FragmentManager fm, int mNumofTab) {
        super(fm);
        this.mNumofTab = mNumofTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MenuListFragment orderFragment = new MenuListFragment();
                return orderFragment;
            case 1:
                ReviewFragment receivedFragment = new ReviewFragment();
                return receivedFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumofTab;
    }
}
