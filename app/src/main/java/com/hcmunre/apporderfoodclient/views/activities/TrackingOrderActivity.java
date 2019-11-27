package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.directionHelper.DirectionFinder;
import com.hcmunre.apporderfoodclient.directionHelper.DirectionFinderListener;
import com.hcmunre.apporderfoodclient.directionHelper.Route;
import com.hcmunre.apporderfoodclient.interfaces.LocalStatusDataSource;
import com.hcmunre.apporderfoodclient.interfaces.StatusDataSource;
import com.hcmunre.apporderfoodclient.models.Database.ShipperData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.Shipper;
import com.hcmunre.apporderfoodclient.models.Entity.Status;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrackingOrderActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {
    @BindView(R.id.txt_name_restaurant)
    TextView txt_name_restaurant;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.txt_confirmed)
    TextView txt_confirmed;
    @BindView(R.id.txt_shipper_coming)
    TextView txt_shipper_coming;
    @BindView(R.id.txt_complete)
    TextView txt_complete;
    @BindView(R.id.image_number_one)
    ImageView image_number_one;
    @BindView(R.id.image_number_two)
    ImageView image_number_two;
    @BindView(R.id.image_number_three)
    ImageView image_number_three;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_contact)
    ImageView image_contact;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    LocationManager locationManager;
    double lat, lng;
    String address;
    private static final int REQUEST_LOCATION = 1;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ShipperData shipperData=new ShipperData();
    int count=0;
    Handler handler;
    Runnable runnable;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);
        init();
        trackingOrder();
        contactRestaurant();
        sendRequest();
        startUpdate();
    }

    private void init() {
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //
        toolbar.setTitle(Common.curentOrder.getOrderName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle(new StringBuilder("Id#").append(Common.curentOrder.getId()));
        txt_name_restaurant.setText(Common.curentOrder.getNameRestaurant());
    }
    private void startUpdate(){
//        handler=new Handler();
//        runnable=new Runnable() {
//            @Override
//            public void run() {
//                new getInforShipping().execute();
//                trackingOrder();
//                count++;
//                txt_total_price.setText(count+"");
//                handler.postDelayed(this,3000);
//            }
//        };
//        handler.postDelayed(runnable,3000);
    }

    private void trackingOrder(){
        StatusDataSource cartDataSource=new LocalStatusDataSource(CartData.getInstance(this).statusDao());
        Status status=new Status();
        status.setOrderId(Common.curentOrder.getId());
        status.setStatus(Common.curentOrder.getOrderStatus());
        compositeDisposable.add(
                cartDataSource.insertStatus(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                },throwable -> {
                    Log.d(Common.TAG,"Lỗi"+throwable.getMessage());
                })
        );
        compositeDisposable.add(
                cartDataSource.getAllStatus(Common.curentOrder.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {

                    for (int i=0;i<statuses.size();i++){
                        int status_order=statuses.get(i).getStatus();
                        if(status_order==1){
                            image_number_one.setImageResource(R.drawable.ic_success);
                        }
                        if(status_order==2){
                            image_number_two.setImageResource(R.drawable.ic_success);
                        }
                        if(Common.isConnectedToInternet(this)){
                            new getInforShipping().execute();
                        }else {
                            Common.showToast(this,"Vui lòng kiểm tra kết nối mạng");
                        }
                    }
                },throwable -> {
                    Log.d(Common.TAG,"Lỗi"+throwable.getMessage());
                })
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void contactRestaurant(){
        image_contact.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+Common.currentRestaurant.getmPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else {
                startActivity(intent);
            }

        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setToTalCash(SentTotalCashEvent event) {
        txt_total_price.setText(event.getCash());
    }

    private void sendRequest() {
        GetCurrentUser getCurrentUser = new GetCurrentUser(TrackingOrderActivity.this);
        address = getCurrentUser.getuser();
        try {
            new DirectionFinder(this, address, Common.currentRestaurant.getmAddress()).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            onGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            {
                Location locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationGps != null) {
                    lat = locationGps.getLatitude();
                    lng = locationGps.getLongitude();
                } else {
                    Toast.makeText(this, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
                }
                mMap = googleMap;
                LatLng currentLocation = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(address)
                        .position(currentLocation)));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = new ProgressDialog(TrackingOrderActivity.this);
        progressDialog.setMessage("Vui lòng đợi...");
        progressDialog.show();
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 17));
            ((TextView) findViewById(R.id.txt_duration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.txt_km)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public class getInforShipping extends AsyncTask<String,String, Shipper>{
        @Override
        protected void onPostExecute(Shipper shipper) {
            if(shipper!=null){
                if(shipper.getShippingStatus()==3){
                    image_number_three.setImageResource(R.drawable.ic_success);
                }
            }

        }

        @Override
        protected Shipper doInBackground(String... strings) {
            Shipper shipper;
            shipper=shipperData.getInforShipper(Common.curentOrder.getId());
            return shipper;
        }
    }
}
