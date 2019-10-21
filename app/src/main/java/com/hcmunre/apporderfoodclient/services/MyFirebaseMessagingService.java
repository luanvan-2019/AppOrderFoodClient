package com.hcmunre.apporderfoodclient.services;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.TokenData;
import com.hcmunre.apporderfoodclient.models.Entity.Token;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    CompositeDisposable compositeDisposable;
    TokenData tokenData=new TokenData();
    @Override
    public void onCreate() {
        compositeDisposable=new CompositeDisposable();
        Paper.init(this);
        super.onCreate();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        //ở đây sẽ cập nhật token,để update chúng ta cần fbid
        String fbid=Paper.book().read(Common.REMEMBER_FBID);
        Token token=new Token();
        token.setFBID(fbid);
        token.setToKen(newToken);
        Observable<Integer> listToken=Observable.just(tokenData.updateTokenToServer1(token));
        compositeDisposable.add(
                listToken
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenModel->{
                    //
                },throwable -> {
                    Toast.makeText(this, "[REFRESH TOKEN]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );


    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> dataRecv=remoteMessage.getData();
        if(dataRecv!=null){
            Common.showNotifacation(
                    this,
                    new Random().nextInt(),
                    dataRecv.get(Common.NOTIFI_TITLE),
                    dataRecv.get(Common.NOTIFI_CONTENT),
                    null
            );
        }
        //sử dụng ARC đẻ test
    }
}
