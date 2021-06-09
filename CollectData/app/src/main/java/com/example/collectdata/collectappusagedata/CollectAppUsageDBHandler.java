package com.example.collectdata.collectappusagedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.db.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CollectAppUsageDBHandler {
    private Context context;

    public CollectAppUsageDBHandler(Context context) {
        this.context = context;
    }

    public void insertAppUsageData(AppUsageData appUsageData) {
        Log.i("ollo", "Insert: " + appUsageData.toString());
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
                Constants.KEY_APP_TIME, Constants.KEY_APP_DURATION, Constants.KEY_APP_NAME, Constants.KEY_APP_CATEGORY
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
                            cursor.getInt(3)
                            )
            );
        }
        cursor.close();

        return appUsageDataList;
    }

    public List<AppUsageData> getAppUsageDataBetweenDates(String startDate, String endData) {
        List<AppUsageData> appUsageDataList = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_APP_TIME, Constants.KEY_APP_DURATION, Constants.KEY_APP_NAME, Constants.KEY_APP_CATEGORY
        };

        String selection = Constants.KEY_APP_DATE + "=?";
        String [] selectionArgs = {startDate, endData};
        String sortOrder = Constants.KEY_APP_TIME;

        Cursor cursor = db.query(
                Constants.TABLE_APP_USAGE,
                projection, selection, selectionArgs, null, null, sortOrder
        );
        while(cursor.moveToNext()) {
            appUsageDataList.add(
                    new AppUsageData(
                            startDate,
                            cursor.getString(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getInt(3)
                    )
            );
        }
        cursor.close();

        return appUsageDataList;
    }

    public int getPackageCode(String pkgName) {
        int pkgCode = -1;
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();

        String [] projection = {
                Constants.KEY_UNIQUE_APP_PACKAGE, Constants.KEY_UNIQUE_APP_CODE
        };
        String selection = Constants.KEY_UNIQUE_APP_PACKAGE + "=?";
        String [] selectionArgs = {pkgName};
        String sortOrder = Constants.KEY_UNIQUE_APP_CODE + " desc";


        Cursor cursor = db.query(
                Constants.TABLE_UNIQUE_APP,
                projection, selection, selectionArgs, null, null, sortOrder, "1"
        );
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            pkgCode = cursor.getShort(1);
        }

        return pkgCode;
    }

    public int insertUniquePackageToList(String pkgName) {
        int currCode = getMaxPackageCode() + 1;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_UNIQUE_APP_PACKAGE, pkgName);
        contentValues.put(Constants.KEY_UNIQUE_APP_CODE, currCode);

        db.insert(Constants.TABLE_UNIQUE_APP, null, contentValues);

        return currCode;
    }

    public int getMaxPackageCode() {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = { Constants.KEY_UNIQUE_APP_CODE };

        String sortOrder = Constants.KEY_UNIQUE_APP_CODE + " desc";

        Cursor cursor = db.query(
                Constants.TABLE_UNIQUE_APP,
                projection, null, null, null, null, sortOrder, "1"
        );

        cursor.moveToFirst();

        return cursor.getCount() == 0? 0 : cursor.getShort(0);
    }

    public int getUniquePackageCount() {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(" + Constants.KEY_UNIQUE_APP_PACKAGE + ") from "
                + Constants.TABLE_UNIQUE_APP, null);
        c.moveToFirst();
        return c.getInt(0);
    }

    /**
     * Returns the unique package code if the duration is greater than 0 from the last 120 days
     * @return
     */
    public List<Integer> getUniqueNonZeroPkgCodes() {
        List<Integer> uniquePkgCodes = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.rawQuery("select distinct " + Constants.KEY_APP_CATEGORY + " from " +
                        Constants.TABLE_APP_USAGE + " where " + Constants.KEY_APP_DURATION + " >? and " +
                        Constants.KEY_APP_DATE + " >? order by " + Constants.KEY_APP_CATEGORY + " desc"
                ,
                new String[]{"0", getStartTimeDate(-120)});
        while(c.moveToNext()) {
            uniquePkgCodes.add(c.getInt(0));
        }

        return uniquePkgCodes;
    }

    /**
     * Returns the app usage data from the last 120 days
     * @return
     */
    public List<Double> getAppUsageDurationByCodeForLast120Days(int pkgCode) {
        List<Double> uniquePkgCodes = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
//        Cursor c = db.rawQuery(
//                "select date, " + Constants.KEY_APP_DURATION + " from " + Constants.TABLE_APP_USAGE +
//                        " where " + Constants.KEY_APP_DATE + " >? and " + Constants.KEY_APP_CATEGORY +
//                        "=? order by " + Constants.KEY_APP_DATE + " asc"
//                ,
//                new String[]{ getStartTimeDate(-120), String.valueOf(pkgCode) });
//        while(c.moveToNext()) {
//            Log.i("ollo", c.getString(0) + " , " + c.getLong(1));
//        }
        Cursor c = db.rawQuery(
                "select " + Constants.KEY_APP_DURATION + " from " + Constants.TABLE_APP_USAGE +
                        " where " + Constants.KEY_APP_DATE + " >? and " + Constants.KEY_APP_CATEGORY +
                        "=? order by " + Constants.KEY_APP_DATE + " asc"
                ,
                new String[]{ getStartTimeDate(-120), String.valueOf(pkgCode) });
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

    /**
     * Update SLR intercepts in the table
     * @param pkgCode
     * @param t0
     * @param t1
     * @return
     */
    public void updatePkgCodeIntercepts(int pkgCode, double t0, double t1, String St) {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_UNIQUE_APP_THETA0, t0);
        contentValues.put(Constants.KEY_UNIQUE_APP_THETA1, t1);
        contentValues.put(Constants.KEY_UNIQUE_APP_SESSIONAL, St);

        int i = db.update(Constants.TABLE_UNIQUE_APP, contentValues,
                Constants.KEY_UNIQUE_APP_CODE + "=?",
                new String[]{String.valueOf(pkgCode)});
    }

    public double getLastDaysAppUsageByCode(int pkgCode) {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();

        return 0.0;
    }

    public String getModelParams(int pkgCode) {
        String data = "";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_UNIQUE_APP_THETA0,
                Constants.KEY_UNIQUE_APP_THETA1,
                Constants.KEY_UNIQUE_APP_SESSIONAL
        };

        Cursor cursor = db.query(
                Constants.TABLE_UNIQUE_APP,
                projection, Constants.KEY_UNIQUE_APP_CODE + "=?",
                new String[]{ String.valueOf(pkgCode) }, null, null, null
        );
        while(cursor.moveToNext()) {
            data = cursor.getShort(0) + ";" + cursor.getShort(1) + cursor.getString(2);
        }
        return data;
    }

    // TODO: just for demo
    public void getDEMO() {
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_UNIQUE_APP_CODE,
                Constants.KEY_UNIQUE_APP_THETA0,
                Constants.KEY_UNIQUE_APP_THETA1,
                Constants.KEY_UNIQUE_APP_SESSIONAL
        };

        String sortOrder = Constants.KEY_UNIQUE_APP_CODE + " desc";

        Cursor cursor = db.query(
                Constants.TABLE_UNIQUE_APP,
                projection, null, null, null, null, sortOrder
        );

        while(cursor.moveToNext()) {
            Log.i("ollo", "DATA :: " + cursor.getShort(0) + ", "+
                    cursor.getShort(1) + ", "+ cursor.getShort(2) + ", " +
                    cursor.getString(3));
        }
    }
}
