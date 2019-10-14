package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.views.adapters.MenuAdapter;
import com.hcmunre.apporderfoodclient.views.fragments.DraftOrder;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuRestaurantActivity extends AppCompatActivity {
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
    @BindView(R.id.cartSystem)
    FloatingActionButton btnCartSystem;
    @BindView(R.id.badge_notification)
    NotificationBadge badge_notification;
    CartDataSource cartDataSource;
    CompositeDisposable compositeDisposable;
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
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        btnCartSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuRestaurantActivity.this,CartListActivity.class));
            }
        });
        cartDataSource=new LocalCartDataSource(CartData.getInstance(this).cartDAO());
        getMenuFood();
        countCart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        countCart();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void countCart() {
        cartDataSource.countItemInCart(PreferenceUtils.getEmail(this),
                Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        badge_notification.setText(String.valueOf(integer));
                        Toast.makeText(MenuRestaurantActivity.this, " Count "+integer, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MenuRestaurantActivity.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMenuFood() {
        txtNameRes.setText(Common.currentRestaurant.getmName());
        txt_address.setText(Common.currentRestaurant.getmAddress());
        collapsingToolbarLayout.setTitle(Common.currentRestaurant.getmName());
        compositeDisposable = new CompositeDisposable();
        FoodData foodData = new FoodData();
        final Observable<ArrayList<Menu>> listMenu=Observable.just(foodData.getMenuResFood(Common.currentRestaurant.getmId()));
        compositeDisposable.add(
                listMenu
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(menus -> {
                    menuAdapter = new MenuAdapter(this, menus);
                    txtCountMenu.setText(menuAdapter.getItemCount() + "");
                    recyc_detailfood.setAdapter(menuAdapter);
                    menuAdapter.notifyDataSetChanged();
                },throwable -> {
                    Toast.makeText(this, "Lỗi"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
