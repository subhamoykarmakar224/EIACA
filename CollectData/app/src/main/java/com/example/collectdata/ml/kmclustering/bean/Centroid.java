package com.example.collectdata.ml.kmclustering.bean;

import java.util.Map;

public class Centroid {

    private Map<String, Double> centroid;

    public Centroid(Map<String, Double> centroid) {
        this.centroid = centroid;
    }

    public Map<String, Double> getCentroid() {
        return centroid;
    }

    public void setCentroid(Map<String, Double> centroid) {
        this.centroid = centroid;
    }

    @Override
    public String toString() {
        return "Centroid{" +
                "centroid=" + centroid +
                '}';
    }
}
