package com.hcmunre.apporderfoodclient.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.views.adapters.TabOrderFragmentAdapter;

public class OrderFragment extends Fragment {
    TabLayout tabLayout;
    TabOrderFragmentAdapter tabOrderFragmentAdapter;
    ViewPager viewPager;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,
                container, false);
        tabLayout=view.findViewById(R.id.tab_order);
        recyclerView=view.findViewById(R.id.recyc_order);
        viewPager=view.findViewById(R.id.viewpager_order);
        listOrder();
        return view;
    }
    private void listOrder(){
        tabLayout.addTab(tabLayout.newTab().setText("Lịch sử"));
        tabLayout.addTab(tabLayout.newTab().setText("Đơn nháp"));
        tabOrderFragmentAdapter = new TabOrderFragmentAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(tabOrderFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
