package com.hcmunre.apporderfoodclient.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.Favorite;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.adapters.FavoriteAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.recyc_favorite)
    RecyclerView recyc_favorite;
    @BindView(R.id.txt_order_history)
    TextView txt_order_history;
    @BindView(R.id.img_delivery)
    ImageView img_delivery;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    FoodData foodData=new FoodData();
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyc_favorite.setLayoutManager(linearLayoutManager);
        recyc_favorite.setItemAnimator(new DefaultItemAnimator());
        swipe_layout.setColorSchemeResources(android.R.color.holo_blue_dark);
        txt_order_history.setVisibility(View.GONE);
        img_delivery.setVisibility(View.GONE);
        if(Common.isConnectedToInternet(getActivity())){
            swipe_layout.setOnRefreshListener(() -> new getFavoriteFood().execute());
            new getFavoriteFood().execute();
        }else {
            Common.showToast(getActivity(),getString(R.string.check_internet));
        }
    }
    public class getFavoriteFood extends AsyncTask<String,String, ArrayList<Favorite>>{

        @Override
        protected void onPreExecute() {
            if(!swipe_layout.isRefreshing()){
                swipe_layout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Favorite> favorites) {
            if(favorites.size()>0){
                FavoriteAdapter favoriteAdapter=new FavoriteAdapter(getActivity(),favorites);
                recyc_favorite.setAdapter(favoriteAdapter);
                swipe_layout.setRefreshing(false);
                txt_order_history.setVisibility(View.GONE);
                img_delivery.setVisibility(View.GONE);
            }else {
                txt_order_history.setVisibility(View.VISIBLE);
                img_delivery.setVisibility(View.VISIBLE);
                swipe_layout.setRefreshing(false);
            }
        }

        @Override
        protected ArrayList<Favorite> doInBackground(String... strings) {
            ArrayList<Favorite> favorites;
            favorites=foodData.getFavoriteByUser(PreferenceUtils.getUserId(getActivity()));
            return favorites;
        }
    }

}
