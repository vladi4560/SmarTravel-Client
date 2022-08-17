package com.vladi.karasove.smartravel.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

public class LocationManager {
    private AppCompatActivity activity;
    private LocationRequest locationRequest;
    private CallBackLocation callBackLocation;

    public interface CallBackLocation {
        void locationReady(double longitude, double latitude);
    }

    public LocationManager(AppCompatActivity activity) {
        this.activity = activity;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
    }

    public void setCallBackLocation(CallBackLocation callBackLocation) {
        this.callBackLocation = callBackLocation;
    }

    public boolean isGPSEnabled() {
        android.location.LocationManager locationManager = (android.location.LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    public void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity.getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                task.getResult(ApiException.class);
                Toast.makeText(activity, "GPS is already tured on", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(activity, 2);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have gps
                        break;
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (!isGPSEnabled()) {
            turnOnGPS();
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.getFusedLocationProviderClient(activity)
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        else if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(activity)
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            callBackLocation.locationReady(0.0, 0.0);
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            LocationServices.getFusedLocationProviderClient(activity)
                    .removeLocationUpdates(this);
            if (locationResult.getLocations().size() > 0) {
                int index = locationResult.getLocations().size() - 1;
                double latitude = locationResult.getLocations().get(index).getLatitude();
                double longitude = locationResult.getLocations().get(index).getLongitude();
                if (callBackLocation != null) {
                    callBackLocation.locationReady(latitude, longitude);
                }
            }
        }
    };
}