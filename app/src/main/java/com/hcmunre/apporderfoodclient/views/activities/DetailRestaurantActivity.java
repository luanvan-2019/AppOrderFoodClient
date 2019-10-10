package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.adapters.MenuAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRestaurantActivity extends AppCompatActivity {
    @BindView(R.id.recyc_detailfood)
    RecyclerView recyc_detailfood;
    @BindView(R.id.txtrestaurant)
    TextView txtNameRes;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.txtCountMenu)
    TextView txtCountMenu;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyc_detailfood.setLayoutManager(layoutManager);
        recyc_detailfood.setItemAnimator(new DefaultItemAnimator());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        getIntentRestaurant();
        getMenuFood();
    }

    private void getIntentRestaurant() {
        int id = 0;
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("dataRestaurant");
        txtNameRes.setText(restaurant.getmName());
        txt_address.setText(restaurant.getmAddress());
        collapsingToolbarLayout.setTitle(restaurant.getmName());
        id = restaurant.getmId();
        Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
    }

    private void getMenuFood() {
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("dataRestaurant");
        ArrayList<Menu> listMenu;
        FoodData foodData = new FoodData();
        listMenu = foodData.getMenuResFood(restaurant.getmId());
        menuAdapter = new MenuAdapter(this, listMenu);
        txtCountMenu.setText(menuAdapter.getItemCount() + "");
        recyc_detailfood.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
    }

}
