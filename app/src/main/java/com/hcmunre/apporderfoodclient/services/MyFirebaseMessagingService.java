package com.hcmunre.apporderfoodclient.services;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.TokenData;
import com.hcmunre.apporderfoodclient.models.Entity.Token;

import java.util.ArrayList;

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

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        //ở đây sẽ cập nhật token,để update chúng ta cần fbid
        String fbid=Paper.book().read(Common.REMEMBER_FBID);
        Observable<ArrayList<Token>> list=Observable.just(tokenData.updateTokenToServer1(fbid,newToken));
        list
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokens -> {

                },throwable -> {
                    Toast.makeText(this, "[REQUEST TOKEN]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
//        compositeDisposable.add(tokenData.updateTokenToServer(fbid,newToken)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(tokenModel->{
//                    //
//                },throwable -> {
//                    Toast.makeText(this, "[REQUEST TOKEN]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                })
//        );


    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
