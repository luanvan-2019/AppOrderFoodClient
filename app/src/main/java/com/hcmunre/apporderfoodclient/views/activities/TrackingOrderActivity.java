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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import com.hcmunre.apporderfoodclient.models.Database.OrderData;
import com.hcmunre.apporderfoodclient.models.Database.ShipperData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.Shipper;
import com.hcmunre.apporderfoodclient.models.Entity.Status;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    @BindView(R.id.image_chat)
    ImageView image_chat;
    @BindView(R.id.txt_name)
    TextView txt_name;
    Status status;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    LocationManager locationManager;
    double lat, lng;
    String address;
    private static final int REQUEST_LOCATION = 1;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ShipperData shipperData = new ShipperData();
    int count = 0;
    Handler handler;
    Runnable runnable;
    Timer timer;
    OrderData orderData = new OrderData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);
        init();
        eventClick();
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
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        txt_name_restaurant.setText(Common.curentOrder.getNameRestaurant());
        txt_total_price.setText(new StringBuilder(numberFormat.format(Common.curentOrder.getTotalPrice()) + "").append("đ"));
    }

    private void eventClick() {
        image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackingOrderActivity.this, ChatActivity.class));
            }
        });
    }

    private void startUpdate() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                new getInforShipping().execute();
                getOrderStatus();
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(runnable, 4000);
    }

    private void trackingOrder() {
        if (Common.isConnectedToInternet(this)) {
            if (Common.curentOrder.getOrderStatus() == 5) {
                image_number_one.setImageResource(R.drawable.ic_cancel);
                txt_confirmed.setText("Cửa hàng đã hủy");
            }
        } else {
            Common.showToast(this, "Vui lòng kiểm tra kết nối mạng");
        }
        new getInforShipping().execute();
        getOrderStatus();


    }
    private void getOrderStatus(){
        Order order = orderData.getOrderStatus(Common.curentOrder.getId());
        if (order.getOrderStatus() == 1) {
            image_number_one.setImageResource(R.drawable.ic_success);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void contactRestaurant() {
        image_contact.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + Common.currentRestaurant.getmPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
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
//        try {
//            Shipper shipper1 = shipperData.getInforShipper(Common.curentOrder.getId(), 2);
//            if(shipper1!=null){
//                Log.d("BBB","Shipper Map");
//                new DirectionFinder(this, address, shipper1.getAddress()).execute();
//            }else if (Common.curentOrder.getRestaurantAddress() != null) {
//                Log.d("BBB","Restaurant Map");
//                new DirectionFinder(this, address, Common.curentOrder.getRestaurantAddress()).execute();
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
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
                try {
                    Shipper shipper1 = shipperData.getInforShipper(Common.curentOrder.getId(), 2);
                    if(shipper1!=null){
                        Log.d("BBB","Shipper Map");
                        new DirectionFinder(this, currentLocation, shipper1.getAddress()).execute();
                    }else if (Common.curentOrder.getRestaurantAddress() != null) {
                        Log.d("BBB","Restaurant Map");
                        new DirectionFinder(this, currentLocation, Common.curentOrder.getRestaurantAddress()).execute();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
//                originMarkers.add(mMap.addMarker(new MarkerOptions()
//                        .title(address)
//                        .position(currentLocation)));

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
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
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
        Drawable start = getResources().getDrawable(R.drawable.ic_location);
        BitmapDescriptor marker_start = getMarkerIconFromDrawable(start);
        Drawable end = getResources().getDrawable(R.drawable.ic_location_end);
        BitmapDescriptor marker_end = getMarkerIconFromDrawable(end);
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 17));
            ((TextView) findViewById(R.id.txt_duration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.txt_km)).setText(route.distance.text);
            MapRipple mapRipple = new MapRipple(mMap, route.startLocation, this);
            mapRipple.withNumberOfRipples(3);
            mapRipple.withFillColor(Color.BLUE);
            mapRipple.withStrokewidth(10);
            mapRipple.withDistance(100);
            mapRipple.withRippleDuration(12000);
            mapRipple.withTransparency(0.7f);
            mapRipple.startRippleMapAnimation();
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(marker_start)
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(marker_end)
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
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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

    public class getInforShipping extends AsyncTask<String, String, Shipper> {
        @Override
        protected void onPostExecute(Shipper shipper) {
            if (shipper != null) {
                Shipper shipper1;
                int status = shipper.getShippingStatus();
                if (status == 2) {
                    image_number_one.setImageResource(R.drawable.ic_success);
                    image_number_two.setImageResource(R.drawable.ic_success);
                    shipper1 = shipperData.getInforShipper(Common.curentOrder.getId(), 2);
                    txt_name.setText(shipper1.getName());
                }
                if (status == 3) {
                    shipper1 = shipperData.getInforShipper(Common.curentOrder.getId(), 3);
                    txt_name.setText(shipper1.getName());
                    image_number_one.setImageResource(R.drawable.ic_success);
                    image_number_two.setImageResource(R.drawable.ic_success);
                    image_number_three.setImageResource(R.drawable.ic_success);
                }
            }


        }

        @Override
        protected Shipper doInBackground(String... strings) {
            Shipper shipper;
            shipper = shipperData.getInforShipperOrder(Common.curentOrder.getId());
            return shipper;
        }
    }
}
