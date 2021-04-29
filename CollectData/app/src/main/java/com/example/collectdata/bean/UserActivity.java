package com.example.collectdata.bean;

public class UserActivity {
    private String date;
    private String time;
    private int duration;
    private String activityId;

    public UserActivity(String date, String time, int duration, String activityId) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.activityId = activityId;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                ", activityId='" + activityId + '\'' +
                '}';
    }
}
