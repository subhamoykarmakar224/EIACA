package com.example.collectdata.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.collectdata.Constants;
import com.example.collectdata.db.DBHelper;

public class SharedPreferenceControl {

    Context context;
    private static DBHelper instance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public SharedPreferenceControl(Context c) {
        this.context = c;
        sharedPreferences = context.getSharedPreferences(
                Constants.SHARED_PREF_ID, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void setDate(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, "");
    }
}
