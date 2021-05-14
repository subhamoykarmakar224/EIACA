package com.example.collectdata.collectweatherdata;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.collectdata.bean.weather.Weather;
import com.example.collectdata.db.DBHelper;

public class CollectWeatherDBHandler {

    private Context context;

    public CollectWeatherDBHandler(@Nullable Context context) {
        this.context = context;
    }

    public void insertDBData(Weather weather) {

    }

    public Weather getWeatherData() {
        Weather weather = new Weather();

        return weather;
    }
}
