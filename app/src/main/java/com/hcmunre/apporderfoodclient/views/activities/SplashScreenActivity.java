package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hcmunre.apporderfoodclient.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;


public class SplashScreenActivity extends AppCompatActivity {
    TextView txtsaigonfood;
    Typeface face;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        init();
        Dexter.withActivity(this)
                .withPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            int secondsDelayed = 1;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }, secondsDelayed * 2000);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }
    private void init(){
        Paper.init(this);
        txtsaigonfood = findViewById(R.id.txtLogo);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/fontsplash.ttf");
        txtsaigonfood.setTypeface(face);
    }

}
