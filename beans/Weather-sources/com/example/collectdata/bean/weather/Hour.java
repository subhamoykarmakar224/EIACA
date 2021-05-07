
package com.example.collectdata.bean.weather;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
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
    private Integer precipMm;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("uv")
    @Expose
    private Integer uv;

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
    public Hour(String time, Double tempF, Condition__2 condition, String windDir, Double pressureIn, Integer precipMm, Integer humidity, Integer uv) {
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
