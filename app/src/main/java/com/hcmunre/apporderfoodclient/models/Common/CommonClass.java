package com.hcmunre.apporderfoodclient.models.Common;

import android.app.Activity;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CommonClass {
    public void traloi(String z, Activity ac){
        Toast.makeText(ac,z, Toast.LENGTH_SHORT).show();
    }
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

}
