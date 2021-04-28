package com.example.collectdata;

public class Constants {
    // Notification channel ID
    public static final String NOTIFICATION_CHANNEL_ID = "ForegroundServiceChannelDataCollection";

    public static final int DATA_COLLECT_SERVICE_ID = 188;
    public static final String ACTION_START_DATA_COLLECT_SERVICE = "startServiceDataCollection";
    public static final String ACTION_STOP_DATA_COLLECT_SERVICE = "stopServiceDataCollection";

    public static final String TABLE_DATA = "data";
    public static final String KEY_DATA_DATE = "date";
    public static final String KEY_DATA_TIME = "time";
    public static final String KEY_DATA_LAT = "lat"; // Latitude
    public static final String KEY_DATA_LNG = "lat"; // Longitude
    public static final String KEY_DATA_TEMP = "tmp"; // Temperature at location
    public static final String KEY_DATA_HUMIDITY = "humidity"; // Humidity at location
    public static final String KEY_DATA_RAINFALL = "rainfall"; // Rainfall at location
    public static final String KEY_DATA_EMOTION_CODE = "emotion"; // Emotion from photo
    public static final String KEY_DATA_QUESTION_RES = "mentalstate"; // Mental State from questions
    public static final String KEY_DATA_AMB_LIGHT = "ambientlight"; // Ambient light in Lumen
    public static final String KEY_DATA_AMB_ENV_CAT = "ambientcategory"; // Ambient sound category

    public static final String TABLE_APP_USAGE = "appusagedata";
    public static final String KEY_APP_DATE = "date";
    public static final String KEY_APP_TIME = "time";
    public static final String KEY_APP_DURATION = "duration"; // duration of use
    public static final String KEY_APP_NAME = "appname"; // App in use
    public static final String KEY_APP_CATEGORY = "appcategory"; // App category

    public static final String TABLE_APP_ACTIVITY = "Activity";
    public static final String KEY_ACTIVITY_DATE = "date";
    public static final String KEY_ACTIVITY_TIME = "time";
    public static final String KEY_ACTIVITY_DURATION = "duration"; // duration of activity
    public static final String KEY_ACTIVITY_ID = "activityid"; // activity id

    public static final String TABLE_APP_HR = "HeartBeatHistory";
    public static final String KEY_HR_ACTIVITY_ID = "activityid"; // activity
    public static final String KEY_HR_DATE = "date"; // date
    public static final String KEY_HR_TIME = "time"; // time
    public static final String KEY_HR_VAL = "hrVal"; // Heart rate value
    public static final String KEY_HR_VAL_POSITION = "posturePosition"; // posture position while activity (0 -> 7)
}
