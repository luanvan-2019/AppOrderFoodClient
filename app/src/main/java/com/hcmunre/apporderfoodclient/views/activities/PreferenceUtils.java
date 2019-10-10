package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public PreferenceUtils(){

    }
    public  static  void saveEmail(String email, Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Email",email);
        editor.apply();
    }
    public static  String getEmail(Context context){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("Email",null);
    }
    public static void savePassword(String password,Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Password",password);
        editor.apply();
    }
    public static  String getPassword(Context context){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("Password",null);
    }
}
