package com.example.collectdata.collectactivitylevel;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.collectdata.Constants;
import com.example.collectdata.bean.UserActivity;
import com.example.collectdata.services.ForegroundDataCollection;
import com.example.collectdata.sharedpref.SharedPreferenceControl;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectPhysicalActivityData extends ForegroundDataCollection {

    // Todo: Delete this line:: https://medium.com/@abhiappmobiledeveloper/android-activity-recognition-api-b7f61847d9dc

    private static final String TAG = "CollectPhysicalActivityData :" + "ollo";
    boolean serviceIsRunning;
    private Context context;
    CollectPhysicalActivityLooper physicalActivityLooper;
    CollectPhysicalActivityDBHandler physicalActivityDBHandler;
    SharedPreferenceControl spController;

    // Activity
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;

    public CollectPhysicalActivityData(Context context, CollectPhysicalActivityLooper physicalActivityLooper, SharedPreferenceControl spController) {
        this.context = context;
        this.physicalActivityLooper = physicalActivityLooper;
        this.spController = spController;
        physicalActivityDBHandler = new CollectPhysicalActivityDBHandler(context);
    }

    @SuppressLint("RestrictedApi")
    public void startCollectingPhysicalActivity() {
        serviceIsRunning = true;

        mActivityRecognitionClient = new ActivityRecognitionClient(context);
        mIntentService = new Intent(context, DetectActivityIntentService.class);
        mPendingIntent = PendingIntent.getService(
                context, 12032, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT
        );

        setTaskToStartListening();

        physicalActivityLooper.handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO :: Build the activity data
                while(serviceIsRunning) {
                    String [] dateTime = getCurrentDateTime().split(" ");
                    physicalActivityDBHandler.insertActivityData(new UserActivity(
                            dateTime[0], dateTime[1],
                            spController.getData(Constants.SP_KEY_LATITUDE),
                            spController.getData(Constants.SP_KEY_LONGITUDE),
                            spController.getData(Constants.SP_KEY_CURRENT_ACTIVITY)
                    ));

                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setTaskToStartListening() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                mPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(context,
                        "Successfully requested activity updates",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,
                        "Requesting activity updates failed to start",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void removeTaskToStartListening() {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                mPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(context,
                        "Removed activity updates successfully!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to remove activity updates!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
        // Remove Activity BG task
        if(!serviceIsRunning) {
            removeTaskToStartListening();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
}
