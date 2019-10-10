package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.views.adapters.FoodAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFoodActivity extends AppCompatActivity {
    @BindView(R.id.recyc_listFood)
    RecyclerView recyclerView;
    @BindView(R.id.txtNameMenuRes)
    TextView txtNameMenuRes;
    @BindView(R.id.linearonclick)
    public LinearLayout txtorder;
    @BindView(R.id.txtCountFood_Order)
    public TextView txtCountFood_Order;
    @BindView(R.id.txtTotalPrice)
    public TextView txtTotalPrice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood);
        ButterKnife.bind(this);
        init();

    }
    private void init(){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listFoodOfMenu();
        getIntentMenu();
        listenOnClick();
    }
    private void listenOnClick() {
        txtorder.setOnClickListener(view -> {
            Intent intent = new Intent(ListFoodActivity.this, DetailOrderActivity.class);
            startActivity(intent);

        });
    }
    private void listFoodOfMenu(){
        Intent intent=getIntent();
        Menu menu = (Menu) intent.getSerializableExtra("dataMenu");
        ArrayList<Food> foods=new ArrayList<>();
        FoodData foodData=new FoodData();
        try {
            foods=foodData.getFoodOfMenu(menu.getmId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FoodAdapter adapter=new FoodAdapter(foods,ListFoodActivity.this);
        recyclerView.setAdapter(adapter);

    }
    private void getIntentMenu() {
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra("dataMenu");
        txtNameMenuRes.setText(menu.getmName());
    }
}
