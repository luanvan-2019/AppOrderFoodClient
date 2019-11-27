package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.sql.SQLException;
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
    @BindView(R.id.txt_contact)
    ImageButton txt_contact;
    @BindView(R.id.image_restaurant)
    ImageView image_restaurant;
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
    Progress progress=new Progress();
    FoodData foodData = new FoodData();
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
    protected void onResume() {
        super.onResume();
        countCart();
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void loadMenuByRestaurant(MenuItemEvent event){
        if(event.isSuccess()){
            txtNameRes.setText(event.getRestaurant().getmName());
            txt_address.setText(event.getRestaurant().getmAddress());
            if(event.getRestaurant().getmImage()!=null){
                byte[] decodeString = Base64.decode(event.getRestaurant().getmImage(), Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                image_restaurant.setImageBitmap(decodebitmap);
            }
            collapsingToolbarLayout.setTitle(event.getRestaurant().getmName());
            if(Common.isConnectedToInternet(this)){
                new getMenuByRestaurant(event.getRestaurant().getmId());
            }else {
                Common.showToast(this,"Vui lòng kiểm tra kết nối mạng");
            }
        }
    }
    public class getMenuByRestaurant extends AsyncTask<String,String,ArrayList<Menu>>{

        int restaurantId;

        public getMenuByRestaurant(int restaurantId) {
            this.restaurantId = restaurantId;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            progress.showProgress(MenuActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Menu> menus) {
            menuAdapter = new MenuAdapter(MenuActivity.this, menus);
            recyc_detailfood.setAdapter(menuAdapter);
            menuAdapter.notifyDataSetChanged();
            progress.hideProgress();
        }

        @Override
        protected ArrayList<Menu> doInBackground(String... strings) {
            ArrayList<Menu> menus;
            menus = foodData.getMenuResFood(restaurantId);
            return menus;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
