package com.example.collectdata;

import com.example.collectdata.bean.Posture;

import java.util.HashSet;
import java.util.Hashtable;

public class Constants {
    // Notification channel ID
    public static final String NOTIFICATION_CHANNEL_ID = "ForegroundServiceChannelDataCollection";

    public static final int DATA_COLLECT_SERVICE_ID = 188;
    public static final String ACTION_START_DATA_COLLECT_SERVICE = "startServiceDataCollection";
    public static final String ACTION_STOP_DATA_COLLECT_SERVICE = "stopServiceDataCollection";

    public static final String SHARED_PREF_ID = "DataCollectionSF";
    public static final String SP_KEY_LOCATION_SERVICE = "locationservice";
    public static final String SP_KEY_LATITUDE = "latitude";
    public static final String SP_KEY_LONGITUDE = "longitude";
    public static final String SP_KEY_MOST_RECENT_FORCAST_DATETIME = "forcastdatetime";
    public static final String SP_KEY_MOST_RECENT_USAGE_DATA = "appusagedatetime";
    public static final String SP_KEY_CALL_LOG_FIRST_RUN = "calllogfirstrun";
    public static final String SP_KEY_MOST_RECENT_CALL_LOG_DATE = "mostrecentcalllog";
    public static final String SP_KEY_CURRENT_ACTIVITY = "currentactivitycode";

    // Activity Tracker Constants
    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000;
    public static final int CONFIDENCE = 70;

    // SharedPreference storing the current dayt forcast
    public static final String SP_FORCAST_DATE = "forcastdate";
    public static final String SP_FORCAST_HR_PREFIX = "forcasthour";
    public static final String SP_FORCAST_HR_1 = "forcasthour1";
    public static final String SP_FORCAST_HR_2 = "forcasthour2";
    public static final String SP_FORCAST_HR_3 = "forcasthour3";
    public static final String SP_FORCAST_HR_4 = "forcasthour4";
    public static final String SP_FORCAST_HR_5 = "forcasthour5";
    public static final String SP_FORCAST_HR_6 = "forcasthour6";
    public static final String SP_FORCAST_HR_7 = "forcasthour7";
    public static final String SP_FORCAST_HR_8 = "forcasthour8";
    public static final String SP_FORCAST_HR_9 = "forcasthour9";
    public static final String SP_FORCAST_HR_10 = "forcasthour10";
    public static final String SP_FORCAST_HR_11 = "forcasthour11";
    public static final String SP_FORCAST_HR_12 = "forcasthour12";
    public static final String SP_FORCAST_HR_13 = "forcasthour13";
    public static final String SP_FORCAST_HR_14 = "forcasthour14";
    public static final String SP_FORCAST_HR_15 = "forcasthour15";
    public static final String SP_FORCAST_HR_16 = "forcasthour16";
    public static final String SP_FORCAST_HR_17 = "forcasthour17";
    public static final String SP_FORCAST_HR_18 = "forcasthour18";
    public static final String SP_FORCAST_HR_19 = "forcasthour19";
    public static final String SP_FORCAST_HR_20 = "forcasthour20";
    public static final String SP_FORCAST_HR_21 = "forcasthour21";
    public static final String SP_FORCAST_HR_22 = "forcasthour22";
    public static final String SP_FORCAST_HR_23 = "forcasthour23";
    public static final String SP_FORCAST_HR_24 = "forcasthour24";

    public static final String TABLE_DATA = "data";
    public static final String KEY_DATA_DATE = "date";
    public static final String KEY_DATA_TIME = "time";
    public static final String KEY_DATA_LAT = "lat"; // Latitude
    public static final String KEY_DATA_LNG = "lng"; // Longitude
    public static final String KEY_DATA_TEMP = "temp"; // Temperature at location
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

    public static final String TABLE_USER_ACTIVITY = "Activity";
    public static final String KEY_ACTIVITY_DATE = "date";
    public static final String KEY_ACTIVITY_TIME = "time";
    public static final String KEY_ACTIVITY_ACTIVITY_CODE = "activitytype"; // duration of activity
//    public static final String KEY_ACTIVITY_DURATION = "duration"; // duration of activity
//    public static final String KEY_ACTIVITY_ID = "activityid"; // activity id

    public static final String TABLE_APP_HR = "HeartBeatHistory";
    public static final String KEY_HR_ACTIVITY_ID = "activityid"; // activity
    public static final String KEY_HR_DATE = "date"; // date
    public static final String KEY_HR_TIME = "time"; // time
    public static final String KEY_HR_VAL = "hrVal"; // Heart rate value
    public static final String KEY_HR_VAL_POSITION = "posturePosition"; // posture position while activity (0 -> 7)

    public static final String TABLE_CALL_HISTORY = "CallHistoryDurations";
    public static final String KEY_CALL_HISTORY_DATE = "calldate";
    public static final String KEY_CALL_HISTORY_TIME = "calltimerange"; // start time with range of 1 hour
    public static final String KEY_CALL_HISTORY_DURATION = "duration"; // duration of call in that seconds


    static final Hashtable<String, Integer> POSTURE_VALS = new Hashtable<>();

    static {
        POSTURE_VALS.put("STILL_LYING_DOWN", 1);
        POSTURE_VALS.put("STILL_SITTING", 2);
        POSTURE_VALS.put("STILL_STANDING", 3);
        POSTURE_VALS.put("WALKING", 4);
        POSTURE_VALS.put("RUNNING", 5);
        POSTURE_VALS.put("ON_BICYCLE", 6);
        POSTURE_VALS.put("IN_VEHICLE", 7);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    // https://www.weatherapi.com/my/
    // http://api.weatherapi.com/v1/current.json?key=156612811a1f4ff7b83174349210605&q=33.6416431,-117.8330669&aqi=no
    // http://api.weatherapi.com/v1/forecast.json?key=156612811a1f4ff7b83174349210605&q=33.6416431,-117.8330669&days=1&aqi=no&alerts=no
    public static final String WEATHER_API_REPLACE_STR ="___________";
    public static final String WEATHER_API_KEY="156612811a1f4ff7b83174349210605";
    public static final String WEATHER_BASE_API="https://api.weatherapi.com/v1/";
    public static final String WEATHER_NOW_API="current.json?key=" + WEATHER_API_KEY + "&q=" + WEATHER_API_REPLACE_STR + "&aqi=no";
    public static final String WEATHER_DAILY_HOURLY_FORECAST_API="forecast.json?key=" + WEATHER_API_KEY + "&q=" + WEATHER_API_REPLACE_STR + "&days=1&aqi=no&alerts=no";
}
