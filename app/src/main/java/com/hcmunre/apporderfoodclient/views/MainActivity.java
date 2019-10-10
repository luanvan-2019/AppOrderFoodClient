package com.hcmunre.apporderfoodclient.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hcmunre.apporderfoodclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
