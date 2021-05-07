package com.example.collectdata.collectappusagedata;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CollectAppUsageData {
    private static final String TAG = "CollectAppUsageStatistics :" + "ollo";
    private Context context;
    boolean serviceIsRunning;
    CollectAppUsageLooper appUsageLooper;

    public CollectAppUsageData(Context context, CollectAppUsageLooper appUsageLooper) {
        this.context = context;
        this.appUsageLooper = appUsageLooper;
    }

    public void getTodaysAppUsage() {
        serviceIsRunning = true;
        appUsageLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                getAppUsage(getStartTime(), getEndTime());
                try {
                    Thread.sleep(24 * 60 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAppUsage(long startMillis, long endMillis) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startMillis, endMillis);
        for(Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {
            Log.i(TAG, "Package :: " + entry.getKey() + " Usage :: " + entry.getValue().describeContents());
            Log.i(TAG, "Package :: " + entry.getValue().getTotalTimeInForeground());
            Log.i(TAG, "Package :: ------------------------------------------");
            // TODO :: Add these data to database if not already saved into DB
            // TODO :: Update SharedPreference last update
        }
    }

    /**
     * return format "2021/05/06 00:00:01"
     */
    private long getStartTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, -1);
        Log.i(TAG, "START TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }

    /**
     * return format "2021/05/06 23:59:59";
     */
    private long getEndTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.add(Calendar.DATE, -1);
        Log.i(TAG, "END TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }
}
