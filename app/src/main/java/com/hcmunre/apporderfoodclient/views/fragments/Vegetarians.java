package com.hcmunre.apporderfoodclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

public class Vegetarians extends Fragment {


    Integer mImage[] = {R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat};
    String mName[] = {"Heo", "Boò", "Gà xối mở", "Gà ác hầm thuốc bắc", "Cá lóc kho tộ"};
    String mPrice[] = {"$ 50.00", "$ 45.00", "$ 29.00", "$ 35.00", "$15.00"};

    private ArrayList<Restaurant> restaurantArrayList;
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allfood, container, false);
        return view;


    }
}
