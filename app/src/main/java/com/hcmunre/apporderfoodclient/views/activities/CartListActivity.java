package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.eventbus.CalculatePriceEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;
import com.hcmunre.apporderfoodclient.views.adapters.CartAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartListActivity extends AppCompatActivity {
    @BindView(R.id.recyc_order)
    RecyclerView recyc_order;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;
    @BindView(R.id.btnOrder)
    TextView btnOrder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Locale localeVN;
    NumberFormat currencyVN;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyc_order.setLayoutManager(layoutManager);
        recyc_order.setItemAnimator(new DefaultItemAnimator());
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Giỏ hàng");

        //chuyển sang dạng tiền việt nam
        localeVN = new Locale("vi", "VN");
        currencyVN = NumberFormat.getInstance(localeVN);
        //
        compositeDisposable=new CompositeDisposable();
        cartDataSource=new LocalCartDataSource(CartData.getInstance(this).cartDAO());
        btnOrder.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new SentTotalCashEvent((txtTotalPrice.getText().toString())));
            startActivity(new Intent(CartListActivity.this, DetailOrderActivity.class));
        });
        getItemInCart();
    }
    private void getItemInCart(){
        compositeDisposable.add(cartDataSource.getAllCart(PreferenceUtils.getEmail(this),
                Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if(cartItems.isEmpty()){
                        btnOrder.setText("Giỏ hàng trống");
                        btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                        btnOrder.setEnabled(false);
                    }else {
                        btnOrder.setText("Giao hàng");
                        btnOrder.setEnabled(true);
                        btnOrder.setBackgroundResource(R.drawable.background_button_cart);
                        CartAdapter cartAdapter=new CartAdapter(this,cartItems);
                        recyc_order.setAdapter(cartAdapter);
                        CalculateCartTotalPrice();
                    }
                }, throwable -> {
                    Toast.makeText(this, "[GET CART]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );
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

    //event bus

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void calculatePrice(CalculatePriceEvent event){
        if(event!=null){
            CalculateCartTotalPrice();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
