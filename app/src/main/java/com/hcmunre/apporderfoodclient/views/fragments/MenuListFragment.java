package com.hcmunre.apporderfoodclient.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.commons.Progress;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.hcmunre.apporderfoodclient.views.adapters.MenuAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MenuListFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyc_detailfood)
    RecyclerView recyc_detailfood;
    MenuAdapter menuAdapter;
    FoodData foodData=new FoodData();
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    public MenuListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menu_list, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyc_detailfood.setLayoutManager(layoutManager);
        recyc_detailfood.setItemAnimator(new DefaultItemAnimator());
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(false);
            }
        });
        swipe_layout.setRefreshing(false);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void loadMenuByRestaurant(MenuItemEvent event){
        if(event.isSuccess()){
            Log.d("BBB","MenuFramget"+event.getRestaurant().getmId());
            if(Common.isConnectedToInternet(getActivity())){
                new getMenuByRestaurant(event.getRestaurant().getmId());
            }else {
                Common.showToast(getActivity(),"Vui lòng kiểm tra kết nối mạng");
            }
        }
    }
    public class getMenuByRestaurant extends AsyncTask<String,String, ArrayList<Menu>> {
        Progress progress=new Progress();
        int restaurantId;

        public getMenuByRestaurant(int restaurantId) {
            this.restaurantId = restaurantId;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            progress.showProgress(getActivity());
        }

        @Override
        protected void onPostExecute(ArrayList<Menu> menus) {
            menuAdapter = new MenuAdapter(getActivity(), menus);
            recyc_detailfood.setAdapter(menuAdapter);
            menuAdapter.notifyDataSetChanged();
            progress.hideProgress();
        }

        @Override
        protected ArrayList<Menu> doInBackground(String... strings) {
            ArrayList<Menu> menus;
            menus = foodData.getMenuResFood(restaurantId);
            return menus;
        }
    }

}
