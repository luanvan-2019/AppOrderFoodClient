package com.hcmunre.apporderfoodclient.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.views.fragments.AccountFragment;
import com.hcmunre.apporderfoodclient.views.fragments.FravoriteFragment;
import com.hcmunre.apporderfoodclient.views.fragments.HomeFragment;
import com.hcmunre.apporderfoodclient.views.fragments.OrderFragment;

public class HomeActivity extends AppCompatActivity {
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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_hơme:
                            selectedFragment = new HomeFragment();

                            break;
                        case R.id.nav_assignment:
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new FravoriteFragment();
                            break;
                        case R.id.nav_user:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}
