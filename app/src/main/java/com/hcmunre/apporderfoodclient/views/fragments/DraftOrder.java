package com.hcmunre.apporderfoodclient.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.eventbus.CalculatePriceEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;
import com.hcmunre.apporderfoodclient.views.activities.DetailOrderActivity;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.adapters.CartAdapter;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DraftOrder extends Fragment {
    private ArrayList<Restaurant> restaurantArrayList;
    private RestaurantAdapter restaurantAdapter;
    @BindView(R.id.recyc_order)
    RecyclerView recyc_order;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;
    @BindView(R.id.btnOrder)
    Button btnOrder;
    Unbinder unbinder;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;
    public DraftOrder() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_item_order,container,false);
        unbinder= ButterKnife.bind(this,view);
        init();
        return  view;
    }
    private void init(){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyc_order.setLayoutManager(layoutManager);
        recyc_order.setItemAnimator(new DefaultItemAnimator());
        compositeDisposable=new CompositeDisposable();
        cartDataSource=new LocalCartDataSource(CartData.getInstance(getActivity()).cartDAO());
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new SentTotalCashEvent(txtTotalPrice.getText().toString()));
                startActivity(new Intent(getActivity(), DetailOrderActivity.class));
            }
        });
        getItemInCart();
    }
    private void getItemInCart(){
        compositeDisposable.add(cartDataSource.getAllCart(PreferenceUtils.getEmail(getActivity()),
                    Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if(cartItems.isEmpty()){
                        btnOrder.setText("Giỏ hàng trống");
                        btnOrder.setEnabled(false);
                        btnOrder.setBackgroundResource(R.color.colorGreyLight);
                    }else {
                        btnOrder.setText("Đặt hàng");
                        btnOrder.setEnabled(true);
                        btnOrder.setBackgroundResource(R.color.colorPrimary);
                        CartAdapter cartAdapter=new CartAdapter(getActivity(),cartItems);
                        recyc_order.setAdapter(cartAdapter);
                        CalculateCartTotalPrice();
                    }
                }, throwable -> {
                    Toast.makeText(getActivity(), "[GET CART]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void CalculateCartTotalPrice() {
        cartDataSource.sumPrice(PreferenceUtils.getEmail(getActivity()),Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        if(aLong==0){
                            btnOrder.setText("Giỏ hàng trống");
                            btnOrder.setEnabled(false);
                            btnOrder.setBackgroundResource(R.color.colorGreyLight);
                        }else {
                            btnOrder.setText("Đặt hàng");
                            btnOrder.setEnabled(true);
                            btnOrder.setBackgroundResource(R.color.colorPrimary);
                        }
                        txtTotalPrice.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e.getMessage().contains("Query return empty"))
                            txtTotalPrice.setText("0");
                        else
                            Toast.makeText(getActivity(), "SUM CART"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
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
}
