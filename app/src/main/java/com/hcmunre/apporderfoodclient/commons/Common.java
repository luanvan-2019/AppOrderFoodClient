package com.hcmunre.apporderfoodclient.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hcmunre.apporderfoodclient.models.Entity.ListMenu;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.Entity.User;

public class Common {
    public static User currentUser;
    public static Restaurant currentRestaurant;
    public static final String KEY_USER="data_user";
    public static final String KEY_RESTAURANT="data_restaurant";
    public static  final String REMEMBER_FBID="FBID";
    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null){

                for(int i=0; i<info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
