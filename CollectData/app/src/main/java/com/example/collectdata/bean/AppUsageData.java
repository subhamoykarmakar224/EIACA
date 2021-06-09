package com.example.collectdata.bean;

public class AppUsageData {
    private String date;
    private String time;
    private long duration;
    private String appName;
    private int appCategory;

    public AppUsageData(String date, String time, long duration, String appName, int appCategory) {
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

    public long getDuration() {
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

    public int getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(int appCategory) {
        this.appCategory = appCategory;
    }

    @Override
    public String toString() {
//        return date + ", " + time + ", " + duration + "," + appName + ", " + appCategory + "";
        return "AppUsageData{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                ", appName='" + appName + '\'' +
                ", appCategory='" + appCategory + '\'' +
                '}';
    }
}
