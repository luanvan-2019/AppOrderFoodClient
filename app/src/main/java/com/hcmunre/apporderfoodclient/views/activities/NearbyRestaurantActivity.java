package com.hcmunre.apporderfoodclient.views.activities;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NearbyRestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private GoogleMap map;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    Marker userMarker;
    RestaurantData restaurantData;
    boolean isFirstLoad=false;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurant);
        init();
    }
    private void init(){
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cửa hàng gần tôi");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    private void buildLocationCallback() {
        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation=locationResult.getLastLocation();
                adMarker(locationResult.getLastLocation());
                if(!isFirstLoad){
                    isFirstLoad=!isFirstLoad;
                    Restaurant restaurant=new Restaurant();
                    restaurant.setmLat(locationResult.getLastLocation().getLatitude());
                    restaurant.setmLng(locationResult.getLastLocation().getLongitude());
                    requestNearbyRestaurant(restaurant,10);
                }
            }
        };

    }

    private void requestNearbyRestaurant(Restaurant restaurant, int distance) {
        restaurantData=new RestaurantData();
        Observable<ArrayList<Restaurant>> listRestaurant=Observable.just(restaurantData.getNearbyRestaurant(restaurant,distance));
        compositeDisposable.add(
                listRestaurant
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    adRestaurantMarker(restaurants);
                },throwable -> {
                    Toast.makeText(this, "[NEARBY RESTAURANT]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void adRestaurantMarker(ArrayList<Restaurant> restaurants) {
        for(Restaurant restaurant:restaurants){
            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_marker))
                    .position(new LatLng(restaurant.getmLat(),restaurant.getmLng()))
                    .snippet(restaurant.getmAddress())
                    .title(new StringBuilder()
                    .append(restaurant.getmId())
                    .append(".")
                    .append(restaurant.getmName()).toString()));

        }
    }

    private void adMarker(Location lastLocation) {
        if(userMarker!=null){
            userMarker.remove();
        }
        LatLng userLatLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        userMarker=map.addMarker(new MarkerOptions().position(userLatLng).title(Common.currentUser.getmName()));
        CameraUpdate yourLocation=CameraUpdateFactory.newLatLngZoom(userLatLng,17);
        map.animateCamera(yourLocation);
    }

    private void buildLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)  {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
