package com.hcmunre.apporderfoodclient.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.commons.Progress;
import com.hcmunre.apporderfoodclient.models.Database.OrderData;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.views.adapters.HistoryCartAdatper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryOrder extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyc_history_order)
    RecyclerView recyc_history_order;
    @BindView(R.id.txt_order_history)
    TextView txt_order_history;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    OrderData orderData = new OrderData();
    Progress progress = new Progress();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyc_history_order.setLayoutManager(linearLayoutManager);
        recyc_history_order.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        getAllOrder();
    }

    private void getAllOrder() {
        Observable<ArrayList<Order>> listOrderHistory = Observable.just(orderData.getAllOrder(Common.currentUser.getId()));
        txt_order_history.setVisibility(View.GONE);
        progress.showProgress(getActivity());
        Handler handler = new Handler(Looper.myLooper());
        compositeDisposable.add(
                listOrderHistory
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(orders -> {
                            handler.postDelayed(() -> {
                                if (orders.size() > 0) {
                                    HistoryCartAdatper historyCartAdatper = new HistoryCartAdatper(getActivity(), orders);
                                    recyc_history_order.setAdapter(historyCartAdatper);
                                    txt_order_history.setVisibility(View.GONE);
                                    progress.hideProgress();
                                } else {
                                    txt_order_history.setVisibility(View.VISIBLE);
                                    progress.hideProgress();
                                }
                            }, 2000);

                        }, throwable -> {
                            Toast.makeText(getActivity(), "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.hideProgress();
                        })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
