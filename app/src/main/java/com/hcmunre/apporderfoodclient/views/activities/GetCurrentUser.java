package com.hcmunre.apporderfoodclient.views.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCurrentUser {
    LocationManager locationManager;
    String lattitue, lontitue;
    private Context context;
    private Geocoder geocoder;
    private List<Address> addressList;
    private static final int REQUEST_LOCATION = 1;

    public GetCurrentUser(Context context) {
        this.context = context;
    }

    public String getuser() {
        String address = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            onGPS();
        } else {
            address = getcurrentLocation();
        }
        return address;
    }

    private String getcurrentLocation() {
        String fullAddress = null;
        geocoder = new Geocoder(context, Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGps != null) {
                double lat = locationGps.getLatitude();
                double lng = locationGps.getLongitude();

                lattitue = String.valueOf(lat);
                lontitue = String.valueOf(lng);
                try {
                    addressList = geocoder.getFromLocation(lat, lng, 1);
                    String address = addressList.get(0).getAddressLine(0);
                    String area = addressList.get(0).getLocality();
                    String city = addressList.get(0).getAdminArea();
                    String country = addressList.get(0).getCountryName();
                    fullAddress = address + "," + area + "," + city + "," + country;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
            }
        }
        return fullAddress;
    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
