
package com.example.collectdata.bean.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("temp_f")
    @Expose
    private Double tempF;
    @SerializedName("condition")
    @Expose
    private Condition condition;
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
    public Current() {
    }

    /**
     * 
     * @param tempF
     * @param precipMm
     * @param uv
     * @param condition
     * @param humidity
     * @param pressureIn
     */
    public Current(Double tempF, Condition condition, Double pressureIn, Integer precipMm, Integer humidity, Integer uv) {
        super();
        this.tempF = tempF;
        this.condition = condition;
        this.pressureIn = pressureIn;
        this.precipMm = precipMm;
        this.humidity = humidity;
        this.uv = uv;
    }

    public Double getTempF() {
        return tempF;
    }

    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
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
