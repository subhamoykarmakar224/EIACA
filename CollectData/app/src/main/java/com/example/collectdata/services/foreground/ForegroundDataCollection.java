package com.example.collectdata.services.foreground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.collectdata.Constants;
import com.example.collectdata.MainActivity;
import com.example.collectdata.R;
import com.example.collectdata.collectactivitylevel.CollectPhysicalActivityData;
import com.example.collectdata.collectactivitylevel.CollectPhysicalActivityLooper;
import com.example.collectdata.collectappusagedata.CollectAppUsageLooper;
import com.example.collectdata.collectappusagedata.CollectAppUsageData;
import com.example.collectdata.collectcallusagedata.CollectCallData;
import com.example.collectdata.collectcallusagedata.CollectCallDataLooper;
import com.example.collectdata.collectlocation.CollectLocationLooper;
import com.example.collectdata.collectlocation.CollectionLocationData;
import com.example.collectdata.collectweatherdata.CollectWeatherData;
import com.example.collectdata.collectweatherdata.CollectWeatherLooper;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

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
    CollectionLocationData locationData;

    // Weather
    CollectWeatherLooper weatherLooper;
    CollectWeatherData weatherData;

    // App Usage
    CollectAppUsageLooper appUsageLooper;
    CollectAppUsageData appUsageData;

    // Call Usage
    CollectCallDataLooper callDataLooper;
    CollectCallData callData;

    // Physical Activity
    CollectPhysicalActivityLooper physicalActivityLooper;
    CollectPhysicalActivityData physicalActivityData;

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

        // Start Notification
        startNotification();

        // Init Loopers
        initLoopers();

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

    private void initLoopers() {
        locationLooper = new CollectLocationLooper();
        locationLooper.start();

        weatherLooper = new CollectWeatherLooper();
        weatherLooper.start();

        appUsageLooper = new CollectAppUsageLooper();
        appUsageLooper.start();

        callDataLooper = new CollectCallDataLooper();
        callDataLooper.start();

        physicalActivityLooper = new CollectPhysicalActivityLooper();
        physicalActivityLooper.start();
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
        stopDataCollectionLoopers();

        // Stop Foreground services
        stopSelf();
    }

    private void startDataCollections() {
        // Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationData = new CollectionLocationData(
                context, locationLooper, locationManager, 1, 1, spController
        );
        locationData.getLocation();

        // Weather
        weatherData = new CollectWeatherData(context, weatherLooper, spController);
        weatherData.getWeatherData(spController.getData(Constants.SP_KEY_LATITUDE), spController.getData(Constants.SP_KEY_LONGITUDE));

        // App Usage
        appUsageData = new CollectAppUsageData(context, appUsageLooper, spController);
        appUsageData.getTodaysAppUsage();

        // Call Log Usage
        callData = new CollectCallData(context, callDataLooper, spController);
        callData.getYesterdaysCallHistory();

        // Physical activity
        physicalActivityData = new CollectPhysicalActivityData(context, physicalActivityLooper, spController);
        physicalActivityData.startCollectingPhysicalActivity();
    }

    private void stopDataCollectionLoopers() {
        locationLooper.looper.quit();
        locationData.setServiceIsRunning(false);

        weatherLooper.looper.quit();
        weatherData.setServiceIsRunning(false);

        appUsageLooper.looper.quit();
        appUsageData.setServiceIsRunning(false);

        callDataLooper.looper.quit();
        callData.setServiceIsRunning(false);

        physicalActivityLooper.looper.quit();
        physicalActivityData.setServiceIsRunning(false);
    }
}
