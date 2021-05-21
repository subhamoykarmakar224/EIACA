package com.example.collectdata.collectappusagedata;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CollectAppUsageData {
    private static final String TAG = "CollectAppUsageStatistics :" + "ollo";
    private Context context;
    boolean serviceIsRunning;
    CollectAppUsageLooper appUsageLooper;
    SharedPreferenceControl spController;
    CollectAppUsageDBHandler appUsageDBHandler;

    public CollectAppUsageData(Context context, CollectAppUsageLooper appUsageLooper, SharedPreferenceControl spController) {
        this.context = context;
        this.appUsageLooper = appUsageLooper;
        this.spController = spController;
        appUsageDBHandler = new CollectAppUsageDBHandler(context);
    }

    public void getTodaysAppUsage() {
        serviceIsRunning = true;
        appUsageLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                while(serviceIsRunning) {
                    getAppUsage(getStartTime(), getEndTime());
                    try {
                        Thread.sleep(24 * 60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getAppUsage(long startMillis, long endMillis) {
        String dt = getStartTimeDate();

        // Check if already inserted into the database then do it next time
        if(spController.getData(Constants.SP_KEY_MOST_RECENT_USAGE_DATA).equalsIgnoreCase(dt))
            return;
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startMillis, endMillis);
        for(Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {
//            Log.i(TAG, "Package :: " + entry.getKey() + " Usage :: " + entry.getValue().describeContents());
//            Log.i(TAG, "Package :: " + entry.getValue().getTotalTimeInForeground());
//            Log.i(TAG, "Package :: ------------------------------------------");

            // Add these data to database
            appUsageDBHandler.insertAppUsageData(new AppUsageData(
                    dt, "00:00:00", // TODO :: change model
                    entry.getValue().getTotalTimeInForeground(),
                    entry.getKey(),
                    "" // TODO :: get app category
            ));


        }
        // Update SharedPreference last update
        spController.setData(Constants.SP_KEY_MOST_RECENT_USAGE_DATA, dt);
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
//        Log.i(TAG, "START TIME :: " + c.getTime().getTime());
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
//        Log.i(TAG, "END TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }

    private String getStartTimeDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }
}
