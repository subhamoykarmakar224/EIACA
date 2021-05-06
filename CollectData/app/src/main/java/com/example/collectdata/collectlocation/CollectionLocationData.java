package com.example.collectdata.collectlocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.collectdata.Constants;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

public class CollectionLocationData implements LocationListener {

    private static final String TAG = "CollectionLocationData :" + "ollo";
    private Context context;
    private CollectLocationLooper locationLooper;
    private LocationManager locationManager;
    private Location locationGPS;
    boolean serviceIsRunning;
    private int minTimeBtwUpdates, minDistanceChange;
    private SharedPreferenceControl spController;


    public CollectionLocationData(Context context, CollectLocationLooper locationLooper, LocationManager locationManager, int minTimeBtwUpdates,
                                  int minDistanceChange, SharedPreferenceControl spController) {
        this.context = context;
        this.locationLooper = locationLooper;
        this.locationManager = locationManager;
        this.minTimeBtwUpdates = minTimeBtwUpdates;
        this.minDistanceChange = minDistanceChange;
        this.spController = spController;
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        serviceIsRunning = true;
        locationLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                while(serviceIsRunning) {
                    locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            minTimeBtwUpdates,
                            minDistanceChange,
                            CollectionLocationData.this
                    );
                    locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    onLocationChanged(locationGPS);
                    try {
                        Thread.sleep(minTimeBtwUpdates*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            spController.setData(Constants.SP_KEY_LONGITUDE, String.valueOf(location.getLongitude()));
            spController.setData(Constants.SP_KEY_LATITUDE, String.valueOf(location.getLatitude()));
        } catch (Exception e) {
            Log.i(TAG, "Location manager error : " + e.getMessage());
            spController.setData(Constants.SP_KEY_LONGITUDE, "");
            spController.setData(Constants.SP_KEY_LATITUDE, "");
        }
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }
}
