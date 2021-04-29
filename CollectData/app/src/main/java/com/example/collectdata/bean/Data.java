package com.example.collectdata.bean;

public class Data {
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private String humidity;
    private String Rainfall;
    private String EmotionCode;
    private String QsRes;
    private String AmbientLight;
    private String AmbientEnvCat;

    public Data(String date, String time, String latitude, String longitude, String humidity, String rainfall, String emotionCode, String qsRes, String ambientLight, String ambientEnvCat) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.humidity = humidity;
        Rainfall = rainfall;
        EmotionCode = emotionCode;
        QsRes = qsRes;
        AmbientLight = ambientLight;
        AmbientEnvCat = ambientEnvCat;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRainfall() {
        return Rainfall;
    }

    public void setRainfall(String rainfall) {
        Rainfall = rainfall;
    }

    public String getEmotionCode() {
        return EmotionCode;
    }

    public void setEmotionCode(String emotionCode) {
        EmotionCode = emotionCode;
    }

    public String getQsRes() {
        return QsRes;
    }

    public void setQsRes(String qsRes) {
        QsRes = qsRes;
    }

    public String getAmbientLight() {
        return AmbientLight;
    }

    public void setAmbientLight(String ambientLight) {
        AmbientLight = ambientLight;
    }

    public String getAmbientEnvCat() {
        return AmbientEnvCat;
    }

    public void setAmbientEnvCat(String ambientEnvCat) {
        AmbientEnvCat = ambientEnvCat;
    }

    @Override
    public String toString() {
        return "Data{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", humidity='" + humidity + '\'' +
                ", Rainfall='" + Rainfall + '\'' +
                ", EmotionCode='" + EmotionCode + '\'' +
                ", QsRes='" + QsRes + '\'' +
                ", AmbientLight='" + AmbientLight + '\'' +
                ", AmbientEnvCat='" + AmbientEnvCat + '\'' +
                '}';
    }
}
