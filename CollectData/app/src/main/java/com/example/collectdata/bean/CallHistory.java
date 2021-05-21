package com.example.collectdata.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CallHistory {
    private String date;
    private String time;
    private String duration;

    public CallHistory(String date, String time, String duration) {
        this.date = date;
        this.time = time;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
//        return convertEpochToDateTimre(date) + ", " + time + ", " + duration;
        return "CallHistory{" +
                "date='" + convertEpochToDateTimre(date) + '\'' +
                ", time='" + time + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    private String convertEpochToDateTimre(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }
}
