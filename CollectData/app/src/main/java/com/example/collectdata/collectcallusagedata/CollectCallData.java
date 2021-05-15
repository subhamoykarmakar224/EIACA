package com.example.collectdata.collectcallusagedata;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.example.collectdata.Constants;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CollectCallData {

    private static final String TAG = "CollectCallData :" + "ollo";
    private Context context;
    boolean serviceIsRunning;
    CollectCallDataLooper callDataLooper;
    SharedPreferenceControl spController;
    HashMap<String, Integer> callDurationHistory;

    public CollectCallData(Context context, CollectCallDataLooper callDataLooper, SharedPreferenceControl spController) {
        this.context = context;
        this.callDataLooper = callDataLooper;
        this.spController = spController;
    }

    public void getYesterdaysCallHistory() {
        serviceIsRunning = true;
        initializeHashMapForDuration();
        callDataLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                while(serviceIsRunning) {
                    getCallUsage();
                    try {
                        Thread.sleep(24 * 60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initializeHashMapForDuration() {
        callDurationHistory = new HashMap<>();
        for(int i = 0; i < 24 ;i ++) {
            if(i < 10) {
                callDurationHistory.put("0".concat(String.valueOf(i)), 0);
            } else {
                callDurationHistory.put(String.valueOf(i), 0);
            }
        }
    }

    public void getCallUsage() {
        Cursor c = null;
        if(spController.getData(Constants.SP_KEY_MOST_RECENT_CALL_LOG_DATE).equals(String.valueOf(getEndTime())))
            return;

        if(!spController.getData(Constants.SP_KEY_CALL_LOG_FIRST_RUN).equals("1")) { // first run
            Log.i(TAG, "Getting All Call Data...");
            c = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, null,
                    null, null,
                    CallLog.Calls.DATE + " ASC"
            );

            // TODO :: Add extra logic for verification
            spController.setData(Constants.SP_KEY_CALL_LOG_FIRST_RUN, "1");
        } else { // not first run
            Log.i(TAG, "Getting Last Day's Call Data...");
            c = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, null,
                    CallLog.Calls.DATE + " between ? and ? ",
                    new String[]{ String.valueOf(getStartTime()), String.valueOf(getEndTime())},
                    CallLog.Calls.DATE + " ASC"
            );
        }
        Log.i(TAG, "No of Calls: " + c.getCount());

//        String name= c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));// name
        while(c.moveToNext()) {
            String phNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String callType = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
            String callDate = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
            String callDuration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
            // CALL DATA:: +15857293531, 1, 2021/02/06 17:38, 3292
//            Log.i(TAG, "CALL DATA:: " + phNumber + ", " + callType + ", " + convertEpochToDateTimre(callDate) + ", " + callDuration);
            if(callType.equalsIgnoreCase("1") || callType.equalsIgnoreCase("2")) {
                String hr = convertEpochToDateTimre(callDate).split(" ")[1];
                hr = hr.substring(0, hr.indexOf(":"));
                callDurationHistory.put(hr, callDurationHistory.get(hr) + Integer.parseInt(callDuration));
            }
        }

        // Insert the call history for that date in to the DB
        CollectCallDataDBHandler callDataDBHandler = new CollectCallDataDBHandler(context);
        callDataDBHandler.insertCallDurationHistory(String.valueOf(getStartTime()), callDurationHistory);

        spController.setData(Constants.SP_KEY_MOST_RECENT_CALL_LOG_DATE, String.valueOf(getEndTime()));
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }

    private String convertEpochToDateTimre(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
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
        c.add(Calendar.DATE, -100);
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
        c.add(Calendar.DATE, -100);
//        Log.i(TAG, "END TIME :: " + c.getTime().getTime());
        return c.getTime().getTime();
    }
}
