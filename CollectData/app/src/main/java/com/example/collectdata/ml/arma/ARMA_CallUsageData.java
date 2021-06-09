package com.example.collectdata.ml.arma;

import android.content.Context;
import android.util.Log;

import com.example.collectdata.collectappusagedata.CollectAppUsageDBHandler;
import com.example.collectdata.collectcallusagedata.CollectCallDataDBHandler;
import com.example.collectdata.ml.arma.slr.SLR;

import java.util.ArrayList;
import java.util.List;

public class ARMA_CallUsageData {
    private static final String TAG = "ARMA_AppUsageData:ollo";
    Context context;
    List<Integer> uniqueCodes;
    CollectCallDataDBHandler callDataDBHandler;

    // Variables
    int resolution;
    List<Integer> t;
    List<Double> Yt;
    List<Double> MA_r;
    List<Double> CMA_r;
    List<Double> St_It;
    List<Double> St;
    List<Double> desessionlize;
    List<Double> Tt;
    double forecast;
    double theta0, theta1;
    SLR slr;

    public ARMA_CallUsageData(Context context, int resolution) {
        this.context = context;
        callDataDBHandler = new CollectCallDataDBHandler(this.context);
        this.resolution = resolution;
        t = new ArrayList<>();
        Yt = new ArrayList<>();
        MA_r = new ArrayList<>();
        CMA_r = new ArrayList<>();
        St_It = new ArrayList<>();
        St = new ArrayList<>();
        desessionlize = new ArrayList<>();
        Tt = new ArrayList<>();
        forecast = 0.0;
        theta0 = 0.0;
        theta1 = 0.0;
    }

    public void startARMACalc() {
        // Step 0 :: Get data for the package code
        Yt = callDataDBHandler.getCallLogUsageForLast120Days();

        // Step 1 :: Calculate Moving Average
        calculateMovingAverage();

        // Step 2 :: Calculate Centre Moving Average
        calculateCentreMovingAvg();

        // Step 3 :: Calculate the (Sessional, Irregular) component
        calculateSt_It();

        // Step 4 :: Calculate the Sessional component
        calculateSt();

        // Step 5 :: Calculate the desessionalized value
        calculateDesessionalize();

        // Step 6 :: Run SLR to get theta0 and theta1
        calculateSLR();

        // TODO :: Step 7 :: Update the values of SLR to database
        updateThetaValuesInDB(theta0, theta1, St);

//         Step 8 :: Calculate trend
//            calculateTrend();
//
//         Calculate forecast
//            calculateForecast(Yt.size());

//            break;
    }

    /**
     * Step 1 : Calculate Moving Average
     */
    private void calculateMovingAverage() {
        double sum = 0;
        for (int i = 0; i < Yt.size(); i++) {
            try {
                for (int j = i; j < i + resolution; j++) {
                    sum += Yt.get(j);
                }
            } catch (Exception e) {
                continue;
            }

//            System.out.println("MA: " + (sum / resolution));
            MA_r.add(sum / resolution);
            sum = 0;
        }
    }

    /**
     * Step 2 : Calculate Centre Moving Average
     */
    private void calculateCentreMovingAvg() {
        double sum = 0;
        for (int i = 0; i < MA_r.size(); i++) {
            try {
                for (int j = i; j < i + 2; j++) {
                    sum += MA_r.get(j);
                }
            } catch (Exception e) {
                continue;
            }

//            System.out.println("CMA: " + (sum / 2.0));
            CMA_r.add((sum / 2.0));
            sum = 0;
        }
    }

    /**
     * Calculate the (Sessional, Irregular) component
     * (Yt/CMA)
     */
    private void calculateSt_It() {
        int cnt = 0;
        for (int i = (resolution / 2); i < Yt.size(); i++) {
            try {
                if(Yt.get(i) == 0 || CMA_r.get(cnt) == 0)
                    St_It.add(0.0);
                else
                    St_It.add(Yt.get(i) / CMA_r.get(cnt));
                cnt++;
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * Calculate the Sessional component
     */
    private void calculateSt() {
        int cnt = 0;
        double[] tmp = new double[Yt.size()];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = 0;
        for (int i = resolution / 2; i < Yt.size(); i++) {
            try {
                tmp[i] = St_It.get(cnt);
                cnt++;
            } catch (Exception e) {
                tmp[i] = 0.0;
            }
        }
        for (int i = 0; i < resolution; i++)
            St.add(0.0);
        int tIndex = 0;
        for (int i = 0; i < tmp.length; i++) {
            tIndex = i % resolution;
            St.set(tIndex, St.get(tIndex) + tmp[i]);
        }
        for (int i = 0; i < St.size(); i++) {
            St.set(i, St.get(i) / (resolution - 1));
        }
    }

    /**
     * Calculate the Yt/St
     */
    private void calculateDesessionalize() {
        for (int i = 0; i < Yt.size(); i++) {
            desessionlize.add(Yt.get(i) / St.get(i % resolution));
        }
    }

    /**
     * Calculate the SLR and get the theta0 and theta1
     */
    private void calculateSLR() {
        double[] x = new double[Yt.size()];
        double[] y = new double[Yt.size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = i + 1;
            y[i] = desessionlize.get(i);
        }
        slr = new SLR(x, y);
        theta0 = slr.intercept();
        theta1 = slr.slope();
    }

    private void updateThetaValuesInDB(double t0, double t1, List<Double> Sesst) {
        String Ssst = "";
        for(Double s : Sesst) {
            Ssst = Ssst + ";" + s;
        }

        Double d0 = new Double(theta0);
        Double d1 = new Double(theta1);

        if(d0.isNaN()) d0 = 0.0;
        if(d1.isNaN()) d1 = 0.0;

        // TODO :: Update value
        callDataDBHandler.updateCallModelIntercepts(d0, d1, Ssst);
    }

    /**
     * Calculate the trend
     * @param t
     * @return
     */
    private double calculateTrend(int t) {
//        for (int i = 0; i < Yt.size() + 1; i++) {
//            Tt.add(theta0 + (theta1 * (i + 1)));
//        }
        return (theta0 + (theta1 * t));
    }

    /**
     * Calculate the forecast St/Tt
     * @return
     */
    private double calculateForecast(int t) {
//        for (int i = 0; i < Yt.size() + 1; i++) {
//            forecast.add(St.get(i % resolution) * Tt.get(i));
//        }
        forecast = St.get(t % resolution) * calculateTrend(t);
        return forecast;
    }

    public void setTheta0(double theta0) {
        this.theta0 = theta0;
    }

    public void setTheta1(double theta1) {
        this.theta1 = theta1;
    }


    public void setSt(List<Double> st) {
        St = st;
    }

    public double getCustomForecast() {
        return forecast;
    }

    private void cleanUpVariables() {
        t.clear();
        Yt.clear();
        MA_r.clear();
        CMA_r.clear();
        St_It.clear();
        St.clear();
        desessionlize.clear();
        Tt.clear();
        forecast = 0.0;
        theta0 = 0.0;
        theta1 = 0.0;
    }
}
