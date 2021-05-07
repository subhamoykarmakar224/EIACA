package com.example.collectdata;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.collectdata.services.ForegroundDataCollection;

public class MainActivity extends AppCompatActivity {

    // permission components
    private static final int REQUEST_CODE = 11023;
    // Class components
    private static Context context;
    private static String[] PERMISSIONS = {
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.PACKAGE_USAGE_STATS
    };
    Intent intentService;
    // Widgets
    private Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all components
        initializeComponents();

        // Runtime permission check
        checkRuntimePermissions();

        // TODO :: get Weather forcast
        // TODO :: ask user to do the questions

        // TODO :: App Usage


    }

    private void initializeComponents() {
        // Variables
        context = MainActivity.this;

        // Widgets
        btnStart = findViewById(R.id.btnStartService);
        btnStop = findViewById(R.id.btnStopService);

        // Check if service running
        if (isMyServiceRunning(ForegroundDataCollection.class))
            btnStartComponentHelper();
        else
            btnStopComponentHelper();

        intentService = new Intent(this, ForegroundDataCollection.class);

        // Check if GPS enable
        enableGPS();

        // Chech if GPS Package usage enabled
        enablePackageUsagePermission();
    }

    /**
     * Prompt user to enable GPS
     */
    private void enableGPS() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void enablePackageUsagePermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    1610);
        }
    }

    public void btnStartService(View v) {
        btnStartComponentHelper();

        if (!isMyServiceRunning(ForegroundDataCollection.class)) {
            ContextCompat.startForegroundService(MainActivity.this, intentService);
        }
    }

    public void btnStopService(View v) {
        btnStopComponentHelper();
        stopService(intentService);
    }

    public void btnTakeSelfie(View view) {
        Intent intent = new Intent(context, ActivityTakeSelfie.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Checks Runtime permission for the application
     */
    private void checkRuntimePermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_CODE);
        for (String p : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, p) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{p},
                        REQUEST_CODE);
            }
        }
    }

    /**
     * Checks if the service is still running in the background
     *
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void btnStartComponentHelper() {
        btnStart.setEnabled(Boolean.FALSE);
        btnStop.setEnabled(Boolean.TRUE);
    }

    private void btnStopComponentHelper() {
        btnStop.setEnabled(Boolean.FALSE);
        btnStart.setEnabled(Boolean.TRUE);
    }

    public void btnLoadQuestionsActivity(View view) {
        Intent intent = new Intent(context, ActivityQuestions.class);
        startActivity(intent);
    }
}