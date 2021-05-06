package com.example.collectdata.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.collectdata.Constants;
import com.example.collectdata.MainActivity;
import com.example.collectdata.R;
import com.example.collectdata.collectlocation.CollectLocationLooper;
import com.example.collectdata.collectlocation.CollectionLocationData;
import com.example.collectdata.sharedpref.SharedPreferenceControl;
import com.google.android.gms.location.FusedLocationProviderClient;

public class ForegroundDataCollection extends Service {
    private static final String TAG = "Service :" + "ollo";
    private static final int DATA_COLLECTION_RATE = 2000;
    private static final int POOL_SIZE = 1;

    private static Context context;
    private boolean serviceIsRunning;

    // Shared preference
    SharedPreferenceControl spController;

    // Notification
    private Notification notification;

    // Location
    LocationManager locationManager;
    CollectLocationLooper locationLooper;
    CollectionLocationData collectionLocationData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() :: Start...");
        serviceIsRunning = true;
        context = ForegroundDataCollection.this;
        spController = SharedPreferenceControl.getInstance(context);

        locationLooper = new CollectLocationLooper();
        locationLooper.start();


        // Start Notification
        startNotification();

        // Start Async Data Collection
        startDataCollections();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() :: Create...");
        serviceIsRunning = false;
    }

    /**
     * Start notification service
     */
    private void startNotification() {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0
        );
        notification = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("STARTED")
                .setContentText("Data Collection started!")
                .setSmallIcon(R.drawable.ic_sharp_info_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(Constants.DATA_COLLECT_SERVICE_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Stopped...");
        serviceIsRunning = false;

        // Stop loopers
        locationLooper.looper.quit();
        collectionLocationData.setServiceIsRunning(false);

        stopSelf();
    }

    private void startDataCollections() {
        // Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        collectionLocationData = new CollectionLocationData(
                context, locationLooper, locationManager, 1, 1, spController
        );
        collectionLocationData.getLocation();


    }

}
