package com.hcmunre.apporderfoodclient.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hcmunre.apporderfoodclient.interfaces.LoadingProgress;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel implements LifecycleObserver {
    private Restaurant restaurant;
    RestaurantData rd=new RestaurantData();
    LoadingProgress loadingProgress;
    CompositeDisposable compositeDisposable;
    public RestaurantViewModel() {
    }

    public MutableLiveData<ArrayList<Restaurant>> mRestaurant=new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    @SuppressLint("CheckResult")
    public void getRestaurants() throws SQLException {
        final Observable<ArrayList<Restaurant>> listObserable = Observable.just(rd.getRestaurant());
        listObserable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> notify())
                .subscribe(restaurant->{
                    if(restaurant!=null){
                        mRestaurant.setValue(restaurant);
                    }
                },throwable -> {
                    error.setValue(throwable.getMessage());
                });
    }

}
