package com.hcmunre.apporderfoodclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.LoadingProgress;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.viewmodels.RestaurantViewModel;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryOrder extends Fragment{
    private RestaurantViewModel mViewModel;
    private ArrayList<Restaurant> restaurantArrayList;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recycOrder;
    ProgressBar progress_order;

    public HistoryOrder() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_order, container, false);
        recycOrder = view.findViewById(R.id.recyc_order);
        mViewModel = new RestaurantViewModel();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycOrder.setLayoutManager(layoutManager);
        recycOrder.setItemAnimator(new DefaultItemAnimator());
        restaurantArrayList = new ArrayList<>();
        if (Common.isConnectedToInternet(getContext())) {
            mViewModel.mRestaurant.observe(this, restaurants -> {
                if (restaurants != null) {
                    restaurantArrayList = (ArrayList<Restaurant>) restaurants;
                    restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
                    recycOrder.setAdapter(restaurantAdapter);
                    restaurantAdapter.notifyDataSetChanged();
                }
            });
            try {
                mViewModel.getRestaurants();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

}
