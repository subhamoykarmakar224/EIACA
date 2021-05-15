package com.example.collectdata.collectambientlight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CollectAmbientLightData extends Activity implements SensorEventListener {
    private static final String TAG = "CollectAmbientLightData :" + "ollo";
    private Context context;

    private SensorManager sensorManager;
    private Sensor ambLightSensor;

    public CollectAmbientLightData(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        ambLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public static void getAmbientData() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambLight = event.values[0];
        Log.i(TAG, "Light Sensor Data :: " + ambLight);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
