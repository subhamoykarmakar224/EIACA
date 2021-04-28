package com.example.collectdata.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.collectdata.Constants;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datacollect.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper instance;
    private SQLiteDatabase db;


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_DATA + "( " +
                Constants.KEY_DATA_DATE + " TEXT, " +
                Constants.KEY_DATA_TIME + " TEXT, " +
                Constants.KEY_DATA_LAT + " TEXT, " +
                Constants.KEY_DATA_LNG + " TEXT, " +
                Constants.KEY_DATA_TEMP + " SHORT, " +
                Constants.KEY_DATA_HUMIDITY + " SHORT, " +
                Constants.KEY_DATA_RAINFALL + " SHORT, " +
                Constants.KEY_DATA_EMOTION_CODE + " SHORT, " +
                Constants.KEY_DATA_QUESTION_RES + " SHORT, " +
                Constants.KEY_DATA_AMB_LIGHT + " SHORT, " +
                Constants.KEY_DATA_AMB_ENV_CAT + " SHORT " +
                ")";

        final String SQL_CREATE_APP_USAGE = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_APP_USAGE + "( " +
                Constants.KEY_APP_DATE + " TEXT, " +
                Constants.KEY_APP_TIME + " TEXT, " +
                Constants.KEY_APP_DURATION + " TEXT, " +
                Constants.KEY_APP_NAME + " TEXT, " +
                Constants.KEY_APP_CATEGORY + " SHORT " +
                ")";

        final String SQL_CREATE_ACTIVITY_LVL = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_APP_ACTIVITY + "( " +
                Constants.KEY_ACTIVITY_DATE + " TEXT, " +
                Constants.KEY_ACTIVITY_TIME + " TEXT, " +
                Constants.KEY_ACTIVITY_DURATION + " INTEGER, " +
                Constants.KEY_ACTIVITY_ID + " TEXT " +
                ")";

        final String SQL_CREATE_APP_HR = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_APP_HR + "( " +
                Constants.KEY_HR_ACTIVITY_ID + " TEXT, " +
                Constants.KEY_HR_DATE + " TEXT, " +
                Constants.KEY_HR_TIME + " TEXT, " +
                Constants.KEY_HR_VAL + " TEXT, " +
                Constants.KEY_HR_VAL_POSITION + " SHORT " +
                ")";

        db.execSQL(SQL_CREATE_DATA_TABLE);
        db.execSQL(SQL_CREATE_APP_USAGE);
        db.execSQL(SQL_CREATE_ACTIVITY_LVL);
        db.execSQL(SQL_CREATE_APP_HR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_APP_USAGE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_APP_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_APP_HR);
        onCreate(db);
    }
}
