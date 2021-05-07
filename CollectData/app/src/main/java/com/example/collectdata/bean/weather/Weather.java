
package com.example.collectdata.bean.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Weather() {
    }

    /**
     * 
     * @param current
     * @param location
     * @param forecast
     */
    public Weather(Location location, Current current, Forecast forecast) {
        super();
        this.location = location;
        this.current = current;
        this.forecast = forecast;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

}
