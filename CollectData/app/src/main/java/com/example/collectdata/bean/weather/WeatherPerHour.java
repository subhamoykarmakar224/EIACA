package com.example.collectdata.bean.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherPerHour {
    private String date;
    private String time;
    private Double tempF;
    private Condition__2 condition;
    private String windDir;
    private Double pressureIn;
    private Integer precipMm;
    private Integer humidity;
    private Integer uv;

    public WeatherPerHour(String date, String time, Double tempF, Condition__2 condition, String windDir, Double pressureIn, Integer precipMm, Integer humidity, Integer uv) {
        this.date = date;
        this.time = time;
        this.tempF = tempF;
        this.condition = condition;
        this.windDir = windDir;
        this.pressureIn = pressureIn;
        this.precipMm = precipMm;
        this.humidity = humidity;
        this.uv = uv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTempF() {
        return tempF;
    }

    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    public Condition__2 getCondition() {
        return condition;
    }

    public void setCondition(Condition__2 condition) {
        this.condition = condition;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public Double getPressureIn() {
        return pressureIn;
    }

    public void setPressureIn(Double pressureIn) {
        this.pressureIn = pressureIn;
    }

    public Integer getPrecipMm() {
        return precipMm;
    }

    public void setPrecipMm(Integer precipMm) {
        this.precipMm = precipMm;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }
}
