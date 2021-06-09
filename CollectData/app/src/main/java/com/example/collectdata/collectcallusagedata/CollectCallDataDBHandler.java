package com.example.collectdata.collectcallusagedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.bean.CallHistory;
import com.example.collectdata.db.DBHelper;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectCallDataDBHandler {

    private Context context;

    public CollectCallDataDBHandler(Context context) {
        this.context = context;
    }

    public void insertCallDurationHistory(String date, HashMap<String, Integer> callHistoryDurations) {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        ContentValues contentValues;
        for(Map.Entry<String, Integer> d : callHistoryDurations.entrySet()) {
            contentValues = new ContentValues();
            contentValues.put(Constants.KEY_CALL_HISTORY_DATE, date);
            contentValues.put(Constants.KEY_CALL_HISTORY_TIME, d.getKey());
            contentValues.put(Constants.KEY_CALL_HISTORY_DURATION, String.valueOf(d.getValue()));
            db.insert(Constants.TABLE_CALL_HISTORY, null, contentValues);
        }
    }

    public List<CallHistory> getCallDurationHistory(String date) {
        List<CallHistory> callHistories = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_APP_DATE,
                Constants.KEY_APP_TIME,
                Constants.KEY_CALL_HISTORY_DURATION
        };

        String selection = Constants.KEY_APP_DATE + "=?";
        String [] selectionArgs = {date};
        String sortOrder = Constants.KEY_APP_TIME;

//        Cursor cursor = db.query(
//                Constants.TABLE_APP_USAGE,
//                projection, selection, selectionArgs, null, null, sortOrder
//        );

        Cursor cursor = db.rawQuery("select * from " + Constants.TABLE_CALL_HISTORY, null);
        while(cursor.moveToNext()) {
            callHistories.add(
                    new CallHistory(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2)
                    )
            );
        }
        cursor.close();
        return callHistories;
    }

    /**
     * Get Call History Count
     * @return
     */
    public int getCallHistoryCount() {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + Constants.TABLE_CALL_HISTORY, null);
        return c.getCount();
    }

    public List<Double> getCallLogUsageForLast120Days() {
        List<Double> uniquePkgCodes = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery(
                "select " + Constants.KEY_CALL_HISTORY_DURATION + " from " + Constants.TABLE_CALL_HISTORY +
                        " order by " + Constants.KEY_CALL_HISTORY_DATE + " desc limit 120"
                , null);
        while(c.moveToNext()) {
            uniquePkgCodes.add(Double.parseDouble(String.valueOf(c.getLong(0))));
        }
        return uniquePkgCodes;
    }

    private String getStartTimeDate(@Nullable int timeDelta) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, timeDelta);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public void updateCallModelIntercepts(double theta0, double theta1, String ssst) {
        SharedPreferenceControl spControl = new SharedPreferenceControl(context);
        spControl.setData(Constants.SP_KEY_CALL_LOG_T0, String.valueOf(theta0));
        spControl.setData(Constants.SP_KEY_CALL_LOG_T1, String.valueOf(theta1));
        spControl.setData(Constants.SP_KEY_CALL_LOG_SESS, String.valueOf(ssst));
    }

    public String getCallModelIntercepts() {
        String data = "";
        SharedPreferenceControl spControl = new SharedPreferenceControl(context);
        data = spControl.getData(Constants.SP_KEY_CALL_LOG_T0) + ";" + spControl.getData(Constants.SP_KEY_CALL_LOG_T1) +
                spControl.getData(Constants.SP_KEY_CALL_LOG_SESS);
        return data;
    }
}
