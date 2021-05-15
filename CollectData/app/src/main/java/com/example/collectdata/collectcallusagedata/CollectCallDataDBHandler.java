package com.example.collectdata.collectcallusagedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.db.DBHelper;

import java.util.ArrayList;
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

        ContentValues contentValues = new ContentValues();
        for(Map.Entry<String, Integer> d : callHistoryDurations.entrySet()) {
            contentValues.put(Constants.KEY_CALL_HISTORY_DATE, date);
            contentValues.put(Constants.KEY_CALL_HISTORY_TIME, d.getKey());
            contentValues.put(Constants.KEY_CALL_HISTORY_DURATION, String.valueOf(d.getValue()));
            db.insert(Constants.TABLE_CALL_HISTORY, null, contentValues);
        }

        db.close();

    }

    public List<AppUsageData> getCallDurationHistory(String date) {
        // TODO ::
        return null;
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
}
