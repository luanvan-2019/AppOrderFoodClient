package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.commons.Progress;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.eventbus.CalculatePriceEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.FoodListEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;
import com.hcmunre.apporderfoodclient.views.adapters.FoodAdapter;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodActivity extends AppCompatActivity {
    @BindView(R.id.recyc_listFood)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.badge_notification)
    NotificationBadge badge_notification;
    CartDataSource cartDataSource;
    @BindView(R.id.image_cart)
    ImageView image_cart;
    @BindView(R.id.btnOrder)
    TextView btnOrder;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;
    FoodAdapter adapter;
    Progress progress=new Progress();
    FoodData foodData=new FoodData();
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
        image_cart.setOnClickListener(view -> startActivity(new Intent(FoodActivity.this,CartListActivity.class)));
        cartDataSource=new LocalCartDataSource(CartData.getInstance(this).cartDAO());

        btnOrder.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new SentTotalCashEvent((txtTotalPrice.getText().toString())));
            startActivity(new Intent(FoodActivity.this, DetailOrderActivity.class));
        });
        countCart();
        CalculateCartTotalPrice();
    }
    @Override
    protected void onDestroy() {
        if(adapter!=null){
            adapter.onStop();
        }
        super.onDestroy();
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
    public void listFoodOfMenu(FoodListEvent event){
        if (event.isSuccess()){
            FoodData foodData=new FoodData();
            toolbar.setTitle(event.getCategory().getmName());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            if(Common.isConnectedToInternet(this)){
                new getFoodByMenu(event.getCategory().getmId());
            }else {
                Common.showToast(this,getString(R.string.check_internet));
            }
        }
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void calculatePrice(CalculatePriceEvent event){
        if(event!=null){
            countCart();
            CalculateCartTotalPrice();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public class getFoodByMenu extends AsyncTask<String,String,ArrayList<Food>>{
        int menuId;

        public getFoodByMenu(int menuId) {
            this.menuId = menuId;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            progress.showProgress(FoodActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {
            adapter=new FoodAdapter(foods, FoodActivity.this);
            recyclerView.setAdapter(adapter);
            progress.hideProgress();
        }

        @Override
        protected ArrayList<Food> doInBackground(String... strings) {
            ArrayList<Food> foods = null;
            try {
                foods=foodData.getFoodOfMenu(menuId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return foods;
        }
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
                        if(integer==0){
                            btnOrder.setText("Giỏ hàng trống");
                            btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                            btnOrder.setEnabled(false);
                        }else {
                            btnOrder.setText("Giao hàng");
                            btnOrder.setEnabled(true);
                            btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                        }
                        badge_notification.setText(String.valueOf(integer));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FoodActivity .this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("BBB",e.getMessage()+"");
                    }
                });
    }
    private void CalculateCartTotalPrice() {
        cartDataSource.sumPrice(PreferenceUtils.getEmail(this),Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        if(aLong<=0){
                            btnOrder.setText("Giỏ hàng trống");
                            btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                            btnOrder.setEnabled(false);

                        }else {
                            btnOrder.setText("Giao hàng");
                            btnOrder.setEnabled(true);
                            btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                        }
                        float tongtien=Float.parseFloat(aLong.toString());
                        Common.curentOrder=new Order();
                        Common.curentOrder.setTotalPrice(tongtien);
                        Locale localeVN = new Locale("vi", "VN");
                        NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                        txtTotalPrice.setText(new StringBuilder(currencyVN.format(aLong)).append("đ"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e.getMessage().contains("Query return empty"))
                            txtTotalPrice.setText("0đ");
                        else{
                            Log.d(Common.TAG,"SUM CART "+e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
