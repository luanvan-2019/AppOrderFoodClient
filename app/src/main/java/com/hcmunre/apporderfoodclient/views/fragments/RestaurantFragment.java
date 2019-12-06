package com.hcmunre.apporderfoodclient.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.eventbus.CategoryItemEvent;
import com.hcmunre.apporderfoodclient.views.activities.NearRestaurantActivity;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantFragment extends Fragment {


    TextView txtNearbyRes;
    RecyclerView recyc_listRestaurant;
    ProgressBar progressBar;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
//        ButterKnife.bind(this, view);
        txtNearbyRes=view.findViewById(R.id.txtNearbyRes);
        recyc_listRestaurant=view.findViewById(R.id.recyc_ListRestaurant);
        progressBar=view.findViewById(R.id.progress);
        init();
        return view;
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_listRestaurant.setLayoutManager(layoutManager);
        recyc_listRestaurant.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyc_listRestaurant.getContext(), layoutManager.getOrientation());
        recyc_listRestaurant.addItemDecoration(dividerItemDecoration);
        txtNearbyRes.setOnClickListener(v -> startActivity(new Intent(getActivity(), NearRestaurantActivity.class)));
        if(Common.isConnectedToInternet(getActivity())){
            new getListRestaurant(1).execute();
        }else {
            Common.showToast(getActivity(),getString(R.string.check_internet));
        }
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
    public void loadRestaurant(CategoryItemEvent event){
        if(event.isSuccess()){
            new getListRestaurant(event.getCategory().getId()).execute();
        }
    }
    public  class getListRestaurant extends AsyncTask<String, String, ArrayList<Restaurant>> {
        Integer category;
        public getListRestaurant(Integer category) {
            this.category = category;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants) {
            RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
            recyc_listRestaurant.setAdapter(restaurantAdapter);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            ArrayList<Restaurant> restaurants = new ArrayList<>();
            RestaurantData restaurantData = new RestaurantData();
            try {
                restaurants = restaurantData.getRestaurant(category);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return restaurants;
        }
    }

}
