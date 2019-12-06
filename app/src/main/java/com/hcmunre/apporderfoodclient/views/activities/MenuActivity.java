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
import android.util.Base64;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.hcmunre.apporderfoodclient.models.Entity.FavoriteOnlyId;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.hcmunre.apporderfoodclient.views.adapters.MenuAdapter;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_check_open)
    TextView txt_check_open;
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
        new loadFavoriteByRestaurant().execute();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void loadMenuByRestaurant(MenuItemEvent event){
        if(event.isSuccess()){
            txtNameRes.setText(event.getRestaurant().getmName());
            txt_address.setText(event.getRestaurant().getmAddress());
            Time time_opening=event.getRestaurant().getOpening();
            Time time_closing=event.getRestaurant().getClosing();
            DateFormat dateFormat=new SimpleDateFormat("HH:mm");
            String opening=dateFormat.format(time_opening);
            String closing=dateFormat.format(time_closing);
            txt_time.setText(new StringBuilder()
                    .append(opening)
                    .append("-")
                    .append(closing));
            if(event.getRestaurant().getmImage()!=null){
                image_restaurant.setImageBitmap(Common.getBitmap(event.getRestaurant().getmImage()));
            }
            collapsingToolbarLayout.setTitle(event.getRestaurant().getmName());
            if(Common.isConnectedToInternet(this)){
                new getMenuByRestaurant(event.getRestaurant().getmId());
            }else {
                Common.showToast(this,"Vui lòng kiểm tra kết nối mạng");
            }
        }

    }
    public class loadFavoriteByRestaurant extends AsyncTask<String,String,ArrayList<FavoriteOnlyId>>{
        @Override
        protected void onPostExecute(ArrayList<FavoriteOnlyId> favoriteOnlyIds) {
            if(favoriteOnlyIds.size()>0&&favoriteOnlyIds!=null){
                Common.currentFavorite=favoriteOnlyIds;
            }else {
                Common.currentFavorite=new ArrayList<>();
            }
        }

        @Override
        protected ArrayList<FavoriteOnlyId> doInBackground(String... strings) {
            ArrayList<FavoriteOnlyId> favoriteOnlyIds;
            favoriteOnlyIds=foodData.getFavoriteByRestaurant(PreferenceUtils.getUserId(MenuActivity.this),Common.currentRestaurant.getmId());
            return favoriteOnlyIds;
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
