package com.example.collectdata.collectappusagedata;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.text.SimpleDateFormat;
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
        String dt = getStartTimeDate(-1);
        int pkgCode = 0;

        // Check if already inserted into the database then do it next time
        if(spController.getData(Constants.SP_KEY_MOST_RECENT_USAGE_DATA).equalsIgnoreCase(dt))
            return;

        // Incase of first time installation collect the data from the last 30 days
        if(appUsageDBHandler.getUniquePackageCount() == 0) {
            collectLast120DaysData();
        } else {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startMillis, endMillis);
            for (Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {
//            Log.i(TAG, "Package :: " + entry.getKey() + " Usage :: " + entry.getValue().describeContents());
//            Log.i(TAG, "Package :: " + entry.getValue().getTotalTimeInForeground());
//            Log.i(TAG, "Package :: ------------------------------------------");

                // Get Package code if present in the database
                pkgCode = appUsageDBHandler.getPackageCode(entry.getKey());
                if (pkgCode == -1) {
                    pkgCode = appUsageDBHandler.insertUniquePackageToList(entry.getKey());
                }

                // Add these data to database
                appUsageDBHandler.insertAppUsageData(new AppUsageData(
                        dt, "00:00:00", // TODO :: change model
                        entry.getValue().getTotalTimeInForeground(),
                        entry.getKey(),
                        pkgCode
                ));
            }
        }
        // Update SharedPreference last update
        spController.setData(Constants.SP_KEY_MOST_RECENT_USAGE_DATA, dt);
    }

    private void collectLast120DaysData() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStatsMap;
        int pkgCode = 0;
        for(int i = 120; i >= 1; i --) {
            usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(getStartTime(-i), getEndTime(-i));
            for (Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {

                // Get Package code if present in the database
                pkgCode = appUsageDBHandler.getPackageCode(entry.getKey());
                if (pkgCode == -1) {
                    pkgCode = appUsageDBHandler.insertUniquePackageToList(entry.getKey());
                }
//                Log.i(TAG, "Insert : " + new AppUsageData(
//                        getStartTimeDate(-i), "0", // TODO :: change model
//                        entry.getValue().getTotalTimeInForeground(),
//                        entry.getKey(),
//                        pkgCode
//                ).toString());
//                Log.i(TAG, "------------------------------------------------------------------------------------");

                // Add these data to database
                appUsageDBHandler.insertAppUsageData(new AppUsageData(
                        getStartTimeDate(-i), "0", // TODO :: change model
                        entry.getValue().getTotalTimeInForeground(),
                        entry.getKey(),
                        pkgCode
                ));
            }
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
//        Log.i(TAG, "START TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }

    private long getStartTime(int timeDelta) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, timeDelta);
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

    /**
     * return format "2021/05/06 23:59:59";
     */
    private long getEndTime(int timeDelta) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.add(Calendar.DATE, timeDelta);
//        Log.i(TAG, "END TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }

    private String getStartTimeDate(@Nullable int timeDelta) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, timeDelta);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }
}
