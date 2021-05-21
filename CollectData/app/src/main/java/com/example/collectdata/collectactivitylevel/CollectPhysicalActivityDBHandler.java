package com.example.collectdata.collectactivitylevel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.AppUsageData;
import com.example.collectdata.bean.UserActivity;
import com.example.collectdata.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectPhysicalActivityDBHandler {
    private Context context;

    public CollectPhysicalActivityDBHandler(Context context) {
        this.context = context;
    }

    // TODO :: shift this to DetectActivityIntentService later
    public void insertActivityData(UserActivity userActivity) {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_ACTIVITY_DATE, userActivity.getDate());
        contentValues.put(Constants.KEY_ACTIVITY_TIME, userActivity.getTime());
        contentValues.put(Constants.KEY_DATA_LAT, userActivity.getLatitude());
        contentValues.put(Constants.KEY_DATA_LNG, userActivity.getLongitude());
        if(userActivity.getActivity().length() > 0)
            contentValues.put(Constants.KEY_ACTIVITY_ACTIVITY_CODE, userActivity.getActivity());
        else
            contentValues.put(Constants.KEY_ACTIVITY_ACTIVITY_CODE, "3");

        db.insert(Constants.TABLE_USER_ACTIVITY, null, contentValues);

    }

    public List<UserActivity> getActivityData(String date) {
        List<UserActivity> userActivityList = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_ACTIVITY_DATE,
                Constants.KEY_ACTIVITY_TIME,
                Constants.KEY_DATA_LAT,
                Constants.KEY_DATA_LNG,
                Constants.KEY_ACTIVITY_ACTIVITY_CODE
        };

        String selection = Constants.KEY_APP_DATE + "=?";
        String [] selectionArgs = {date};
        String sortOrder = Constants.KEY_APP_TIME;

        Cursor cursor = db.query(
                Constants.TABLE_USER_ACTIVITY,
                projection, selection, selectionArgs, null, null, sortOrder
        );
        while(cursor.moveToNext()) {
            userActivityList.add(
                    new UserActivity(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    )
            );
        }
        cursor.close();

        return userActivityList;
    }
}

//    private String date;
//    private String time;
//    private int duration;
//    private String latitude;
//    private String longitude;
//    private String activity;