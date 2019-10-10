package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.txtCancel)
    TextView txtCancel;
    @BindView(R.id.editSearch)
    EditText editSearch;
    @BindView(R.id.recyc_search)
    RecyclerView recyc_search;
    CompositeDisposable compositeDisposable;
    RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyc_search.setLayoutManager(layoutManager);
        recyc_search.setHasFixedSize(true);
        clickCancel();
        searchFoodRes();
    }

    public void clickCancel() {
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void searchFoodRes() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String search) {
        RestaurantData restaurantData = new RestaurantData();
        Observable<ArrayList<Restaurant>> listObserable = Observable.just(restaurantData.SearchFoodRes(search));
        listObserable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    restaurantAdapter = new RestaurantAdapter(this, restaurants);
                    restaurantAdapter.notifyDataSetChanged();
                    recyc_search.setAdapter(restaurantAdapter);
                });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
