package com.example.collectdata.collectweatherdata;

import android.content.Context;
import android.util.Log;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.weather.Weather;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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


    public CollectWeatherData(Context context, CollectWeatherLooper weatherLooper) {
        this.context = context;
        this.weatherLooper = weatherLooper;
    }

    public void getWeatherData(String lat, String lon) {
        serviceIsRunning = true;
        weatherLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                while(serviceIsRunning) {
                    getWeatherForcastOfLocation(lat, lon);

                    try {
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
            Weather weather = new Weather();
            Gson gson = new Gson();
            weather = gson.fromJson(jsonString, Weather.class);
//            Log.i(TAG, "DATA :: " + weather
//                    .getForecast().getForecastday().get(0)
//                    .getHour().size()
//            );
            // TODO :: extract data and save weather data SharedPreference


        }catch (IOException e) {
            Log.i(TAG, "Error: " + e.getMessage());
        }
    }

    private void updateMostRecentForcastDate() {
        
    }

    private void insert24HrsForcastIntoDB() {

    }
}
