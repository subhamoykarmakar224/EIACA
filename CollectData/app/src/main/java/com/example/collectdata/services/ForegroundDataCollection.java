package com.example.collectdata.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.collectdata.Constants;
import com.example.collectdata.MainActivity;
import com.example.collectdata.R;
import com.example.collectdata.hractivity.CollectHRActivityData;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ForegroundDataCollection extends Service {
    private static final String TAG = "Service :" + "ollo";
    private static final int DATA_COLLECTION_RATE = 2000;
    private static final int POOL_SIZE = 4;
    private static Context context;
    private boolean serviceIsRunning;
    ThreadPoolExecutor threadPoolExecutor;

    // Notification
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() :: Start...");
        context = ForegroundDataCollection.this;
        startNotification();
        initializeVariables();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() :: Start...");
        serviceIsRunning = false;
    }

    private void initializeVariables() {
        threadPoolExecutor = new ThreadPoolExecutor(
                POOL_SIZE, POOL_SIZE, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
        );
        threadPoolExecutor.execute(new CollectHRActivityData(context));
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
        threadPoolExecutor.shutdown();
    }
}
