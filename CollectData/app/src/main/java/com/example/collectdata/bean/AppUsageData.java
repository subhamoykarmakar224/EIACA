package com.example.collectdata.bean;

public class AppUsageData {
    private String date;
    private String time;
    private int duration;
    private String appName;
    private String appCategory;

    public AppUsageData(String date, String time, int duration, String appName, String appCategory) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.appName = appName;
        this.appCategory = appCategory;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    @Override
    public String toString() {
        return "AppUsageData{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                ", appName='" + appName + '\'' +
                ", appCategory='" + appCategory + '\'' +
                '}';
    }
}
