package com.example.collectdata.collectappusagedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectAppUsageDBHandler {
    private Context context;

    public CollectAppUsageDBHandler(Context context) {
        this.context = context;
    }

    public void insertAppUsageData(AppUsageData appUsageData) {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_APP_DATE, appUsageData.getDate());
        contentValues.put(Constants.KEY_APP_TIME, appUsageData.getTime());
        contentValues.put(Constants.KEY_APP_DURATION, appUsageData.getDuration());
        contentValues.put(Constants.KEY_APP_NAME, appUsageData.getAppName());
        contentValues.put(Constants.KEY_APP_CATEGORY, appUsageData.getAppCategory());

        db.insert(Constants.TABLE_APP_USAGE, null, contentValues);

    }

    public List<AppUsageData> getAppUsageData(String date) {
        List<AppUsageData> appUsageDataList = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_APP_TIME, Constants.KEY_APP_DURATION, Constants.KEY_APP_NAME
        };

        String selection = Constants.KEY_APP_DATE + "=?";
        String [] selectionArgs = {date};
        String sortOrder = Constants.KEY_APP_TIME;

        Cursor cursor = db.query(
                Constants.TABLE_APP_USAGE,
                projection, selection, selectionArgs, null, null, sortOrder
        );
        while(cursor.moveToNext()) {
            appUsageDataList.add(
                    new AppUsageData(
                            date,
                            cursor.getString(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            ""
                            )
            );
        }
        cursor.close();

        return appUsageDataList;
    }
}
