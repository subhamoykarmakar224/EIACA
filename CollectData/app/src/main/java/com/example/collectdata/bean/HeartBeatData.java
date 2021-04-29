package com.example.collectdata.bean;

public class HeartBeatData {
    private String activityId;
    private String date;
    private String time;
    private short heartrate;
    private Posture posture;

    public HeartBeatData(String activityId, String date, String time, short heartrate, Posture posture) {
        this.activityId = activityId;
        this.date = date;
        this.time = time;
        this.heartrate = heartrate;
        this.posture = posture;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
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

    public short getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(short heartrate) {
        this.heartrate = heartrate;
    }

    public Posture getPosture() {
        return posture;
    }

    public void setPosture(Posture posture) {
        this.posture = posture;
    }

    @Override
    public String toString() {
        return "HeartBeatData{" +
                "activityId='" + activityId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", heartrate=" + heartrate +
                ", posture=" + posture +
                '}';
    }
}
