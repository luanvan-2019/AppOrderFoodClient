package com.hcmunre.apporderfoodclient.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodclient.views.fragments.DraftOrder;
import com.hcmunre.apporderfoodclient.views.fragments.HistoryOrder;

public class TabOrderFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumofTab;

    public TabOrderFragmentAdapter(FragmentManager fm, int mNumofTab) {
        super(fm);
        this.mNumofTab = mNumofTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HistoryOrder historyOrder = new HistoryOrder();
                return historyOrder;
            case 1:
                DraftOrder draftOrder = new DraftOrder();
                return draftOrder;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumofTab;
    }
}
