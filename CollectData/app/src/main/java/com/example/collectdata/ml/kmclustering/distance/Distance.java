package com.example.collectdata.ml.kmclustering.distance;

import java.util.Map;

public interface Distance {
    double calculate(Map<String, Double> point1, Map<String, Double> point2);
}
