package com.example.collectdata.collectactivitylevel;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.collectdata.Constants;
import com.example.collectdata.sharedpref.SharedPreferenceControl;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectActivityIntentService extends IntentService {

    protected static final String TAG = DetectActivityIntentService.class.getSimpleName() + "ollo";
    SharedPreferenceControl spController;

    public DetectActivityIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        spController = new SharedPreferenceControl(getApplicationContext());
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        for (DetectedActivity activity : detectedActivities) {
//            Log.i(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
            spController.setData(Constants.SP_KEY_CURRENT_ACTIVITY, String.valueOf(activity.getType()));
        }
    }
}
