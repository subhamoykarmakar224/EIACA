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
//                Constants.KEY_DATA_TEMP + " INT, " +
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

        final String SQL_CREATE_UNIQUE_APPS = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_UNIQUE_APP + "( " +
                Constants.KEY_UNIQUE_APP_PACKAGE + " TEXT, " +
                Constants.KEY_UNIQUE_APP_CODE + " SHORT PRIMARY KEY," +
                Constants.KEY_UNIQUE_APP_THETA0 + " SHORT default 0, " +
                Constants.KEY_UNIQUE_APP_THETA1 + " SHORT default 0," +
                Constants.KEY_UNIQUE_APP_SESSIONAL + " TEXT" +
                ")";


        final String SQL_CREATE_ACTIVITY_LVL = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_USER_ACTIVITY + "( " +
                Constants.KEY_ACTIVITY_DATE + " TEXT, " +
                Constants.KEY_ACTIVITY_TIME + " TEXT, " +
                Constants.KEY_DATA_LAT + " TEXT, " +
                Constants.KEY_DATA_LNG + " TEXT, " +
                Constants.KEY_ACTIVITY_ACTIVITY_CODE + " TEXT " +
                ")";

        final String SQL_CREATE_APP_HR = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_APP_HR + "( " +
                Constants.KEY_HR_ACTIVITY_ID + " TEXT, " +
                Constants.KEY_HR_DATE + " TEXT, " +
                Constants.KEY_HR_TIME + " TEXT, " +
                Constants.KEY_HR_VAL + " TEXT, " +
                Constants.KEY_HR_VAL_POSITION + " SHORT " +
                ")";

        final String SQL_CREATE_CALL_LOG = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_CALL_HISTORY + "( " +
                Constants.KEY_CALL_HISTORY_DATE + " TEXT, " +
                Constants.KEY_CALL_HISTORY_TIME + " TEXT, " +
                Constants.KEY_CALL_HISTORY_DURATION + " TEXT" +
                ")";

        final String SQL_CREATE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_NOTIFICATION + "( " +
                Constants.KEY_NOTIFICATION_UNAME + " INT PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_NOTIFICATION_UNAME + " TEXT, " +
                Constants.KEY_NOTIFICATION_DATE + " TEXT " +
                ")";

        final String SQL_CREATE_CALL_LOG_ML_MODEL = "CREATE TABLE IF NOT EXISTS " +
                Constants.TABLE_CALL_LOG_ML_MODEL + "( " +
                Constants.KEY_CALL_LOG_HR + " INT PRIMARY KEY AUTOINCREMENT, " +
                Constants.KEY_CALL_LOG_THETA0 + " short, " +
                Constants.KEY_CALL_LOG_THETA1 + " short, " +
                Constants.KEY_CALL_LOG_SESSIONAL + " TEXT " +
                ")";

        db.execSQL(SQL_CREATE_UNIQUE_APPS);
        db.execSQL(SQL_CREATE_DATA_TABLE);
        db.execSQL(SQL_CREATE_APP_USAGE);
        db.execSQL(SQL_CREATE_ACTIVITY_LVL);
        db.execSQL(SQL_CREATE_APP_HR);
        db.execSQL(SQL_CREATE_CALL_LOG);
        db.execSQL(SQL_CREATE_CALL_LOG_ML_MODEL);
//        db.execSQL(SQL_CREATE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_APP_USAGE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_UNIQUE_APP);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USER_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_APP_HR);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CALL_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CALL_LOG_ML_MODEL);
//        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NOTIFICATION);
        onCreate(db);
    }
}
