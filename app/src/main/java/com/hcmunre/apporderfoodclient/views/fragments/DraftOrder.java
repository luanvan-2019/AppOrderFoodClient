package com.hcmunre.apporderfoodclient.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;
import com.hcmunre.apporderfoodclient.views.adapters.AllCartAdapter;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DraftOrder extends Fragment {
    @BindView(R.id.recyc_order)
    RecyclerView recyc_order;
    @BindView(R.id.txt_order)
    TextView txt_order;
    @BindView(R.id.image_delivery)
    ImageView image_delivery;
    Unbinder unbinder;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        getItemIncart();
        return view;
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_order.setLayoutManager(layoutManager);
        recyc_order.setItemAnimator(new DefaultItemAnimator());
        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartData.getInstance(getActivity()).cartDAO());

    }

    private void getItemIncart() {
        compositeDisposable.add(cartDataSource.getAllCartItem(PreferenceUtils.getEmail(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allCartItems -> {
                    if (allCartItems.size() > 0) {
                        AllCartAdapter cartAdapter = new AllCartAdapter(getActivity(), allCartItems);
                        recyc_order.setAdapter(cartAdapter);
                        txt_order.setVisibility(View.GONE);
                        image_delivery.setVisibility(View.GONE);
                    } else {
                        txt_order.setVisibility(View.VISIBLE);
                        image_delivery.setVisibility(View.VISIBLE);
                    }

                }, throwable -> {
                    image_delivery.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "[GET ALL CART]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

}
