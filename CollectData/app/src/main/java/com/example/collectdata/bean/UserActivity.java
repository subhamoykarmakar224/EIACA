package com.example.collectdata.bean;

public class UserActivity {
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private String activity;

    public UserActivity(String date, String time, String latitude, String longitude, String activity) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return date + ", " + time + ", " + latitude + ", " + longitude + ", " + activity;
//        return "UserActivity{" +
//                "date='" + date + '\'' +
//                ", time='" + time + '\'' +
//                ", latitude='" + latitude + '\'' +
//                ", longitude='" + longitude + '\'' +
//                ", activity='" + activity + '\'' +
//                '}';
    }
}
