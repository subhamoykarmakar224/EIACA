package com.example.collectdata;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.collectdata.bean.weather.Weather;
import com.example.collectdata.collectappusagedata.CollectAppUsageDBHandler;
import com.example.collectdata.collectcallusagedata.CollectCallDataDBHandler;
import com.example.collectdata.db.DBHelper;
import com.example.collectdata.ml.arma.ARMA_AppUsageData;
import com.example.collectdata.ml.arma.ARMA_CallUsageData;
import com.example.collectdata.services.foreground.ForegroundDataCollection;
import com.example.collectdata.services.network.SendDataToServer;
import com.example.collectdata.sharedpref.SharedPreferenceControl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//public class MainActivity extends AppCompatActivity implements SensorEventListener {
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
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.READ_CALL_LOG
    };
    Intent intentService;
    CollectAppUsageDBHandler collectAppUsageDBHandler;
    CollectCallDataDBHandler callDataDBHandler;

    // Widgets
    private Button btnStart, btnStop;
    private SensorManager sensorManager;
    private Sensor ambLightSensor;
    private TextView textViewWelcome;

    // SharedPreferebce
    SharedPreferenceControl spController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all components
        initializeComponents();

        // Runtime permission check
        checkRuntimePermissions();


        // TODO :: ask user to do the questions

        // TODO :: Ambient light sensor

        // TODO :: Delete all ocde below
//        SharedPreferenceControl spControl = new SharedPreferenceControl(context);
//         -------------- PHYSICAL ACTIVITY --------------
//        CollectPhysicalActivityDBHandler phyDBHandler = new CollectPhysicalActivityDBHandler(context);
//        List<UserActivity> userActivityHistory = phyDBHandler.getActivityData("2021-05-19");
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");
//        for(UserActivity u : userActivityHistory) {
//            Log.i("Mainactivity::ollo", u.toString());
//        }
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");

        // -------------- AMBIENT LIGHT --------------
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        ambLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // -------------- APP USAGE DATA --------------
//        CollectAppUsageDBHandler appUsageDBHandler = new CollectAppUsageDBHandler(context);
//        List<AppUsageData> appUsageData = appUsageDBHandler.getAppUsageData("2021-06-04");
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");
//        for(AppUsageData u : appUsageData) {
//            Log.i("Mainactivity::ollo", u.toString());
//        }
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");

        // -------------- CALL USAGE DATA --------------
//        CollectCallDataDBHandler callDataDBHandler = new CollectCallDataDBHandler(context);
//        List<CallHistory> callHistories = callDataDBHandler.getCallDurationHistory(
//                String.valueOf(getStartTime("2020-08-02".split("-")))
//        );
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");
//        for(CallHistory u : callHistories) {
//
//            Log.i("Mainactivity::ollo", u.toString());
//        }
//        Log.i("Mainactivity::ollo", "--------------------------------------------------------------");
//        CollectAppUsageDBHandler appUsageDBHandler = new CollectAppUsageDBHandler(this);
//        appUsageDBHandler.getDEMO();
    }

    private long getStartTime(String[] date) {
        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
        c.set(Calendar.DATE, Integer.parseInt(date[2]));
        c.set(Calendar.MONTH, Integer.parseInt(date[1]));
        c.set(Calendar.YEAR, Integer.parseInt(date[0]));
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime().getTime();
    }

    private void initializeComponents() {
        // Variables
        context = MainActivity.this;

        // SharedPreference
        spController = new SharedPreferenceControl(context);

        callDataDBHandler = new CollectCallDataDBHandler(context);

        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("Welcome, " + spController.getData(Constants.SP_KEY_MY_UNAME) + "!");

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

        consumeMessageFromBackEnd();
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
//        sensorManager.unregisterListener(this);
        DBHelper.getInstance(context).close();
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

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float ambLight = event.values[0];
//        Log.i("Mainactivity :: ollo :: ", "Light Sensor Data :: " + ambLight);
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        sensorManager.registerListener(this, ambLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }

    // TODO :: Just for demo
    public void onClickBtnTrainModel(View v) {
        ARMA_AppUsageData arma_appUsageData = new ARMA_AppUsageData(this, Constants.RESOLUTION_4);
        arma_appUsageData.startARMACalc();

        ARMA_CallUsageData arma_callUsageData = new ARMA_CallUsageData(this, Constants.RESOLUTION_24);
        arma_callUsageData.startARMACalc();
    }

    // TODO :: Just for demo
    public void onClickBtnEvaluateUser(View v) {
        String[] data;
        boolean goToQs = false;
        int t = 0;
        double t0 = 0.0, t1 = 0.0, forecast = 0.0, actual=0.0;
        List<Double> St = new ArrayList<>();
        collectAppUsageDBHandler = new CollectAppUsageDBHandler(context);
        List<Integer> pkgCodes = collectAppUsageDBHandler.getUniqueNonZeroPkgCodes();
        List<Double> tmpData = new ArrayList<>();

        for (Integer i : pkgCodes) {
            // 29716;-9989;9.806498008697437;8.546729540510745;9.49285767521013;9.84344931115187
            // theta0;theta1;St
            data = collectAppUsageDBHandler.getModelParams(i).split(";");
            t0 = Double.parseDouble(data[0]);
            t1 = Double.parseDouble(data[1]);
            for (int j = 2; j < data.length; j++)
                St.add(Double.parseDouble(data[j]));
            tmpData = collectAppUsageDBHandler.getAppUsageDurationByCodeForLast120Days(i);
            t = tmpData.size();
            forecast = St.get(t % Constants.RESOLUTION_4) * (t0 + (t1 * t));
            if(forecast <= 0)
                forecast = 0;
            actual = tmpData.get(tmpData.size()-1);
            if(Math.abs((forecast - actual)/actual) > Constants.DEVIATION_THRESH) {
//                Log.i("ollo", "Codes : " + i + " => Actual : " + actual);
//                Log.i("ollo", "Codes : " + i + " => Forecast : " + forecast);
//                Log.i("ollo", "-------------------------------------------");
                goToQs = true;
            }
            if(goToQs) {
                showAfterForecastDialogue();
                break;
            }
        }
        if(goToQs)
            return;

        // TODO :: Forecast the call usage data
        data = callDataDBHandler.getCallModelIntercepts().split(";");
        t0 = Double.parseDouble(data[0]);
        t1 = Double.parseDouble(data[1]);
        St.clear();

        for (int j = 2; j < data.length; j++)
            St.add(Double.parseDouble(data[j]));

        tmpData = callDataDBHandler.getCallLogUsageForLast120Days();
        t = tmpData.size();
        forecast = St.get(t % Constants.RESOLUTION_24) * (t0 + (t1 * t));
        if(forecast <= 0)
            forecast = 0;
        try {
            actual = tmpData.get(tmpData.size() - 1);
            if (Math.abs((forecast - actual) / actual) > Constants.DEVIATION_THRESH) {
//                Log.i("ollo", "Codes : " + i + " => Actual : " + actual);
//                Log.i("ollo", "Codes : " + i + " => Forecast : " + forecast);
//                Log.i("ollo", "-------------------------------------------");
                goToQs = true;
            }
        } catch (Exception e) {
            goToQs = false;
        }
        if(goToQs) {
            showAfterForecastDialogue();
            return;
        }
    }

    private void showAfterForecastDialogue() {
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage("Hi There! It seems to me that you are not doing too well. Would you please answer a few questions?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        btnLoadQuestionsActivity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void btnLoadQuestionsActivity() {
        Intent intent = new Intent(context, ActivityQuestions.class);
        startActivity(intent);
    }

    public void onClickBtnShowNotifications(View v) {
        startActivity(new Intent(context, NotificationActivity.class));
    }

    private void consumeMessageFromBackEnd() {
        (new SendDataToServer(context, null))
                .getMyFriendsStatus();
    }
}