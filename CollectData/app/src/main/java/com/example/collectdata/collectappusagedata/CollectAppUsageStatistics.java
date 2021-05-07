package com.example.collectdata.collectappusagedata;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CollectAppUsageStatistics {
    private static final String TAG = "CollectAppUsageStatistics :" + "ollo";
    private Context context;

    public CollectAppUsageStatistics(Context context) {
        this.context = context;
    }

    public void getTodaysAppUsage() {
        String startDate = "2021/05/06 00:00:01";
        String endDate = "2021/05/06 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date sdate = sdf.parse(startDate);
            Date edate = sdf.parse(endDate);
            getAppUsage(sdate.getTime(), edate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getAppUsage(long startMillis, long endMillis) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startMillis, endMillis);
        for(Map.Entry<String, UsageStats> entry : usageStatsMap.entrySet()) {
            Log.i(TAG, "Package :: " + entry.getKey() + " Usage :: " + entry.getValue().describeContents());
            Log.i(TAG, "Package :: " + entry.getValue().getTotalTimeInForeground());
            Log.i(TAG, "Package :: ------------------------------------------");
            // TODO :: Add these data to database
        }
    }
}
