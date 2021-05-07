package com.example.collectdata.collectweatherdata;

import android.os.Handler;
import android.os.Looper;

public class CollectWeatherLooper extends Thread {

    public Handler handler;
    public Looper looper;

    @Override
    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();
        handler = new Handler();
        Looper.loop();
    }
}