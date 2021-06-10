package com.example.collectdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.collectdata.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationDBHandler {
    private Context context;

    public NotificationDBHandler(Context context) {
        this.context = context;
    }

//    public static final String TABLE_NOTIFICATION = "Notification";
//    public static final String KEY_NOTIFICATION_UNAME = "uname";
//    public static final String KEY_NOTIFICATION_DATE = "date";

    public void insertNewNotification(String date, String uname) {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put(Constants.KEY_NOTIFICATION_DATE, date);
        contentValues.put(Constants.KEY_NOTIFICATION_UNAME, uname);
        db.insert(Constants.TABLE_NOTIFICATION, null, contentValues);
    }

    public void clearNotificationTable() {
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        db.delete(Constants.TABLE_NOTIFICATION, null, null);
    }

    public List<String> getNotification() {
        List<String> notifications = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        String [] projection = {
                Constants.KEY_NOTIFICATION_DATE,
                Constants.KEY_NOTIFICATION_UNAME
        };

        Cursor cursor = db.query(
                Constants.TABLE_NOTIFICATION,
                projection, null, null,
                null, null, null
        );
        while(cursor.moveToNext()) {
            notifications.add(
                cursor.getString(0) + ";"  + cursor.getString(1)
            );
        }
        cursor.close();
        return notifications;
    }
}
