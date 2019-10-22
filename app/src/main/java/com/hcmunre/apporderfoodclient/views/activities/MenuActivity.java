package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.commons.Progress;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.hcmunre.apporderfoodclient.views.adapters.MenuAdapter;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuActivity extends AppCompatActivity {
    @BindView(R.id.recyc_detailfood)
    RecyclerView recyc_detailfood;
    @BindView(R.id.txtrestaurant)
    TextView txtNameRes;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.txtCountMenu)
    TextView txtCountMenu;
    @BindView(R.id.txt_contact)
    Button txt_contact;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    MenuAdapter menuAdapter;
    @BindView(R.id.cartSystem)
    FloatingActionButton btnCartSystem;
    @BindView(R.id.badge_notification)
    NotificationBadge badge_notification;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CartDataSource cartDataSource;
    CompositeDisposable compositeDisposable;
    Progress progress=new Progress();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        init();
        contactRestaurant();
    }

    private void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyc_detailfood.setLayoutManager(layoutManager);
        recyc_detailfood.setItemAnimator(new DefaultItemAnimator());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnCartSystem.setOnClickListener(view -> startActivity(new Intent(MenuActivity.this,CartListActivity.class)));
        cartDataSource=new LocalCartDataSource(CartData.getInstance(this).cartDAO());
        countCart();

    }
    private void contactRestaurant(){
        txt_contact.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+Common.currentRestaurant.getmPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else {
                startActivity(intent);
            }

        });
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MenuActivity.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("BBB",e.getMessage()+"");
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void loadMenuByRestaurant(MenuItemEvent event){
        if(event.isSuccess()){
            txtNameRes.setText(Common.currentRestaurant.getmName());
            txt_address.setText(Common.currentRestaurant.getmAddress());
            collapsingToolbarLayout.setTitle(Common.currentRestaurant.getmName());
            compositeDisposable = new CompositeDisposable();
            FoodData foodData = new FoodData();
            final Observable<ArrayList<Menu>> listMenu=Observable.just(foodData.getMenuResFood(event.getRestaurant().getmId()));
            Handler handler=new Handler();
            progress.showProgress(this);
            compositeDisposable.add(
                    listMenu
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(menus -> {
                                handler.postDelayed(() -> {
                                    menuAdapter = new MenuAdapter(MenuActivity.this, menus);
                                    txtCountMenu.setText(menuAdapter.getItemCount() + "");
                                    recyc_detailfood.setAdapter(menuAdapter);
                                    menuAdapter.notifyDataSetChanged();
                                    progress.hideProgress();
                                },1000);

                            },throwable -> {
                                Toast.makeText(this, "Lỗi"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            })
            );
        }
    }
}
