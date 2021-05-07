
package com.example.collectdata.bean.weather;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Day {

    @SerializedName("maxtemp_f")
    @Expose
    private Double maxtempF;
    @SerializedName("mintemp_f")
    @Expose
    private Double mintempF;
    @SerializedName("condition")
    @Expose
    private Condition__1 condition;
    @SerializedName("uv")
    @Expose
    private Integer uv;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Day() {
    }

    /**
     * 
     * @param uv
     * @param condition
     * @param mintempF
     * @param maxtempF
     */
    public Day(Double maxtempF, Double mintempF, Condition__1 condition, Integer uv) {
        super();
        this.maxtempF = maxtempF;
        this.mintempF = mintempF;
        this.condition = condition;
        this.uv = uv;
    }

    public Double getMaxtempF() {
        return maxtempF;
    }

    public void setMaxtempF(Double maxtempF) {
        this.maxtempF = maxtempF;
    }

    public Double getMintempF() {
        return mintempF;
    }

    public void setMintempF(Double mintempF) {
        this.mintempF = mintempF;
    }

    public Condition__1 getCondition() {
        return condition;
    }

    public void setCondition(Condition__1 condition) {
        this.condition = condition;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

}
