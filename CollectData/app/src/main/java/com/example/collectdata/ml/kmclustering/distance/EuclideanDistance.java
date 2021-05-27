package com.example.collectdata.ml.kmclustering.distance;

import java.util.Map;

public class EuclideanDistance implements Distance {

    @Override
    public double calculate(Map<String, Double> point1, Map<String, Double> point2) {
        double sum = 0.0;
        for(String k : point1.keySet()) {
            sum += Math.pow(
                    point1.get(k) - point2.get(k)
                    ,
                    2
            );
        }
        return Math.sqrt(sum);
    }
}
