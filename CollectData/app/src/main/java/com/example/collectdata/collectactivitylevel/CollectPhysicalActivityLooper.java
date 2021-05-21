package com.example.collectdata.collectactivitylevel;

import android.os.Handler;
import android.os.Looper;

public class CollectPhysicalActivityLooper  extends Thread {

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