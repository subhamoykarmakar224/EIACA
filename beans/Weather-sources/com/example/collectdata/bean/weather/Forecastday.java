
package com.example.collectdata.bean.weather;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Forecastday {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day")
    @Expose
    private Day day;
    @SerializedName("astro")
    @Expose
    private Astro astro;
    @SerializedName("hour")
    @Expose
    private List<Hour> hour = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Forecastday() {
    }

    /**
     * 
     * @param date
     * @param astro
     * @param hour
     * @param day
     */
    public Forecastday(String date, Day day, Astro astro, List<Hour> hour) {
        super();
        this.date = date;
        this.day = day;
        this.astro = astro;
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }

}
