package com.example.collectdata.collectweatherdata;

import android.content.Context;
import android.util.Log;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.Data;
import com.example.collectdata.bean.weather.Hour;
import com.example.collectdata.bean.weather.Weather;
import com.example.collectdata.sharedpref.SharedPreferenceControl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * https://www.weatherapi.com/
 * TODO :: Shift this to Job Executor Pool
 */
public class CollectWeatherData {
    private static final String TAG = "CollectWeatherData :" + "ollo";
    private Context context;
    private CollectWeatherLooper weatherLooper;
    boolean serviceIsRunning;
    private URL url;
    private HttpURLConnection urlConnection;
    private SharedPreferenceControl spController;

    private final String [] FORECAST_KEYS = {
    Constants.SP_FORCAST_HR_1, Constants.SP_FORCAST_HR_2, Constants.SP_FORCAST_HR_3,
    Constants.SP_FORCAST_HR_4, Constants.SP_FORCAST_HR_5, Constants.SP_FORCAST_HR_6,
    Constants.SP_FORCAST_HR_7, Constants.SP_FORCAST_HR_8, Constants.SP_FORCAST_HR_9,
    Constants.SP_FORCAST_HR_10, Constants.SP_FORCAST_HR_11, Constants.SP_FORCAST_HR_12,
    Constants.SP_FORCAST_HR_13, Constants.SP_FORCAST_HR_14, Constants.SP_FORCAST_HR_15,
    Constants.SP_FORCAST_HR_16, Constants.SP_FORCAST_HR_17, Constants.SP_FORCAST_HR_18,
    Constants.SP_FORCAST_HR_19, Constants.SP_FORCAST_HR_20, Constants.SP_FORCAST_HR_21,
    Constants.SP_FORCAST_HR_22, Constants.SP_FORCAST_HR_23, Constants.SP_FORCAST_HR_24
    };


    public CollectWeatherData(Context context, CollectWeatherLooper weatherLooper, SharedPreferenceControl spController) {
        this.context = context;
        this.weatherLooper = weatherLooper;
        this.spController = spController;
    }

    public void getWeatherData(String lat, String lon) {
        serviceIsRunning = true;
        weatherLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                while(serviceIsRunning) {
                    getWeatherForcastOfLocation(lat, lon);
                    try {
                        // TODO :: Change this to alarm manager
                        Thread.sleep(24 * 60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }

    private void getWeatherForcastOfLocation(String lat, String lon) {
        // Check if weather forecast already there for today
        if(getCurrentDate().equals(spController.getData(Constants.SP_FORCAST_DATE)))
            return;

        urlConnection = null;
        try {
            url = new URL("https://api.weatherapi.com/v1/forecast.json?key=" +
                    Constants.WEATHER_API_KEY + "&q=" + lat + "," + lon + "&days=1&aqi=no&alerts=no");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            String jsonString = sb.toString();
            // Convert JSON string to Weather object
            Gson gson = new Gson();
            Weather weather = gson.fromJson(jsonString, Weather.class);

            // Save weather hourly forecast data into SharedPreference
            int cnt = 0;
            for(Hour h : weather.getForecast().getForecastday().get(0).getHour()) {
                // Format :: 2021-05-14 00:00;;61.7;;82;;0
//                Log.i(TAG, h.toString());
                spController.setData(FORECAST_KEYS[cnt], h.toString());
                cnt += 1;
            }

            // Save last date when we got the forecast. {format:: 2021-05-14}
            spController.setData(Constants.SP_FORCAST_DATE,
                    weather.getForecast().getForecastday().get(0).getHour().get(0).getTime().split(" ")[0]
            );

        }catch (IOException e) {
            Log.i(TAG, "Error: " + e.getMessage());
        }
    }

    /**
     * Return current date in the following format
     * {format:: 2021-05-14}
     * @return
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
