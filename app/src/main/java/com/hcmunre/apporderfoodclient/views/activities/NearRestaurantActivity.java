package com.hcmunre.apporderfoodclient.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NearRestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private GoogleMap map;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    Marker userMarker;
    RestaurantData restaurantData = new RestaurantData();
    boolean isFirstLoad = false;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cửa hàng gần tôi");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                adMarker(locationResult.getLastLocation());
                if (!isFirstLoad) {
                    isFirstLoad = !isFirstLoad;
                    Restaurant restaurant = new Restaurant();
                    restaurant.setmLat(locationResult.getLastLocation().getLatitude());
                    restaurant.setmLng(locationResult.getLastLocation().getLongitude());
                    restaurant.setDistance(10d);
                    new getNearbyRestaurant(restaurant);
                }
            }
        };

    }

    public class getNearbyRestaurant extends AsyncTask<String,String,ArrayList<Restaurant>>{
        private Restaurant restaurant;

        public getNearbyRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            this.execute();
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants) {
            adRestaurantMarker(restaurants);
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            ArrayList<Restaurant> restaurants;
            restaurants=restaurantData.getNearbyRestaurant(restaurant);
            return restaurants;
        }
    }
    private void adRestaurantMarker(ArrayList<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker_x))
                    .position(new LatLng(restaurant.getmLat(), restaurant.getmLng()))
                    .snippet(restaurant.getmAddress())
                    .title(new StringBuilder()
                            .append(restaurant.getmId())
                            .append(".")
                            .append(restaurant.getmName()).toString()));
        }
    }
    private void adMarker(Location lastLocation) {
        if (userMarker != null) {
            userMarker.remove();
        }
        LatLng userLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        userMarker = map.addMarker(new MarkerOptions().position(userLatLng).title(PreferenceUtils.getName(this)));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(userLatLng, 17);
        map.animateCamera(yourLocation);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Log.d("ERROR_MAP", "Load stype error");
            }
        } catch (Resources.NotFoundException e) {
            Log.d("ERROR_MAP", "Resource not found");
        }
        map.setOnInfoWindowClickListener(marker -> {
            String id = marker.getTitle().substring(0, marker.getTitle().indexOf("."));
            if (!TextUtils.isEmpty(id)) {
                if(Common.isConnectedToInternet(this)){
                    new getRestaurantById(id);
                }else {
                    Common.showToast(this,getString(R.string.check_internet));
                }
            }
        });
    }
    public class getRestaurantById extends AsyncTask<String,String,ArrayList<Restaurant>>{
        private String id;

        public getRestaurantById(String id) {
            this.id = id;
            this.execute();
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants) {
            Common.currentRestaurant = restaurants.get(0);
            EventBus.getDefault().postSticky(new MenuItemEvent(true, Common.currentRestaurant));
            startActivity(new Intent(NearRestaurantActivity.this, MenuActivity.class));
            finish();
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            ArrayList<Restaurant> restaurants;
            restaurants=restaurantData.getRestaurantById(id);
            return restaurants;
        }
    }

}

