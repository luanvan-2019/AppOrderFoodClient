package com.hcmunre.apporderfoodclient.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.commons.Progress;
import com.hcmunre.apporderfoodclient.models.Database.OrderData;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;
import com.hcmunre.apporderfoodclient.views.adapters.HistoryCartAdatper;

import java.sql.SQLException;
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
    @BindView(R.id.img_delivery)
    ImageView img_delivery;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    OrderData orderData = new OrderData();
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
        txt_order_history.setVisibility(View.GONE);
        img_delivery.setVisibility(View.GONE);
        swipe_layout.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipe_layout.setOnRefreshListener(() -> new getAllOrder(swipe_layout).execute());
        if(Common.isConnectedToInternet(getActivity())){
            new getAllOrder(swipe_layout).execute();
        }else {
            Common.showToast(getActivity(),getString(R.string.check_internet));
        }


    }

    public class getAllOrder extends AsyncTask<String, String, ArrayList<Order>> {
        SwipeRefreshLayout swipeRefreshLayout;

        public getAllOrder(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        @Override
        protected void onPreExecute() {

           if(!swipe_layout.isRefreshing()){
               swipe_layout.setRefreshing(true);
           }
        }

        @Override
        protected void onPostExecute(ArrayList<Order> orders) {
            if (orders.size() > 0) {
                HistoryCartAdatper historyCartAdatper = new HistoryCartAdatper(getActivity(), orders);
                recyc_history_order.setAdapter(historyCartAdatper);
                historyCartAdatper.refresh(orders);
                txt_order_history.setVisibility(View.GONE);
                img_delivery.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                txt_order_history.setVisibility(View.VISIBLE);
                img_delivery.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<Order> doInBackground(String... strings) {
            ArrayList<Order> orders;
            orders = orderData.getAllOrder(PreferenceUtils.getUserId(getActivity()));
            return orders;
        }
    }
}
