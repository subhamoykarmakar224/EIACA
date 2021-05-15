
package com.example.collectdata.bean.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hour {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("temp_f")
    @Expose
    private Double tempF;
    @SerializedName("condition")
    @Expose
    private Condition__2 condition;
    @SerializedName("wind_dir")
    @Expose
    private String windDir;
    @SerializedName("pressure_in")
    @Expose
    private Double pressureIn;
    @SerializedName("precip_mm")
    @Expose
    private Double precipMm;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("uv")
    @Expose
    private Double uv;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Hour() {
    }

    /**
     * 
     * @param tempF
     * @param precipMm
     * @param uv
     * @param condition
     * @param humidity
     * @param time
     * @param windDir
     * @param pressureIn
     */
    public Hour(String time, Double tempF, Condition__2 condition, String windDir, Double pressureIn, Double precipMm, Double humidity, Double uv) {
        super();
        this.time = time;
        this.tempF = tempF;
        this.condition = condition;
        this.windDir = windDir;
        this.pressureIn = pressureIn;
        this.precipMm = precipMm;
        this.humidity = humidity;
        this.uv = uv;
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

    public Double getPrecipMm() {
        return precipMm;
    }

    public void setPrecipMm(Double precipMm) {
        this.precipMm = precipMm;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    @Override
    public String toString() {
        return time + ";;" + tempF + ";;" + humidity + ";;" + precipMm;
    }
}
