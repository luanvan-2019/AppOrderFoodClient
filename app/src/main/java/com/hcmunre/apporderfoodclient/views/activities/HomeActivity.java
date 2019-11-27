package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.interfaces.ShowFragment;
import com.hcmunre.apporderfoodclient.views.fragments.AccountFragment;
import com.hcmunre.apporderfoodclient.views.fragments.FravoriteFragment;
import com.hcmunre.apporderfoodclient.views.fragments.HistoryOrder;
import com.hcmunre.apporderfoodclient.views.fragments.HomeFragment;
import com.hcmunre.apporderfoodclient.views.fragments.OrderFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG_HISTORY_ORDER = "tag_history_order";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hcmunre.apporderfoodclient",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav =
            menuItem -> {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_hơme:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_assignment:
                        if(PreferenceUtils.getEmail(this)==null){
                            startActivity(new Intent(this,SignInActivity.class));
                            finish();
                        }else {
                            selectedFragment = new OrderFragment();
                            break;
                        }
                    case R.id.nav_favorites:
                        if(PreferenceUtils.getEmail(this)==null){
                            startActivity(new Intent(this,SignInActivity.class));
                            finish();
                        }else {
                            selectedFragment = new FravoriteFragment();
                            break;
                        }
                    case R.id.nav_user:
                        selectedFragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thoát ứng dụng");
        builder.setMessage("Bạn có muốn thoát không?");
        builder.setCancelable(true);
        builder.setPositiveButton("Thoát", (dialog, which) -> finish());
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog dialog=builder.create();
        dialog.show();
    }


}
