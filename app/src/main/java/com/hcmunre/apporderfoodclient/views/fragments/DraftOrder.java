package com.hcmunre.apporderfoodclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class DraftOrder extends Fragment {
    private ArrayList<Restaurant> restaurantArrayList;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recycOrder;

    public DraftOrder() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_item_order,container,false);
        recycOrder=view.findViewById(R.id.recyc_order);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recycOrder.setLayoutManager(layoutManager);
        recycOrder.setItemAnimator(new DefaultItemAnimator());
        restaurantArrayList =new ArrayList<>();
        RestaurantData restaurantData=new RestaurantData();
        try {
            restaurantArrayList=restaurantData.getRestaurant();
        } catch (SQLException e) {
            e.printStackTrace();
        }restaurantAdapter=new RestaurantAdapter(getActivity(),restaurantArrayList);
        recycOrder.setAdapter(restaurantAdapter);
        return  view;
    }
}
