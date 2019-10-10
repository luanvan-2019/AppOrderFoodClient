package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodclient.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import io.reactivex.disposables.CompositeDisposable;

public class SplashScreenActivity extends AppCompatActivity {
    TextView txtsaigonfood;
    Typeface face;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        int secondsDelayed = 1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                                finish();
                            }
                        }, secondsDelayed * 3000);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(SplashScreenActivity.this, "Bạn phải bật permission để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }
    private void init(){
        txtsaigonfood = findViewById(R.id.txtSaigonfood);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/fontsplash.ttf");
        txtsaigonfood.setTypeface(face);
    }

}
