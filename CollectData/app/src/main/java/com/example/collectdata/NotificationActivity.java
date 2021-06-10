package com.example.collectdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.collectdata.services.network.SendDataToServer;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    NotificationDBHandler notificationDBHandler;
    Boolean isRunning;
    ListView listViewNotification;

    ArrayAdapter<String> adapter;
    Context context;

    TextView textViewNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        textViewNotification = findViewById(R.id.textViewNotification);

        isRunning = true;
        context = this;

        notificationDBHandler = new NotificationDBHandler(this);


        (new SendDataToServer(this, ""))
                .getMyFriendsStatus();

        SharedPreferenceControl spcontrol = new SharedPreferenceControl(this);

        String [] d = spcontrol.getData(Constants.SP_KEY_NOTIF_CONSUMER).split(",");
        d[0] = d[0].substring(1);
        String tmpS = "";
        for(String s : d) {
            tmpS = tmpS + s + "\n\n";
        }
        String finalTmpS = tmpS;
        textViewNotification.setText(finalTmpS);
        textViewNotification.setMovementMethod(new ScrollingMovementMethod());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    String [] d = spcontrol.getData(Constants.SP_KEY_NOTIF_CONSUMER).split(",");
                    d[0] = d[0].substring(1);
                    String tmpS = "";
                    for(String s : d) {
                        tmpS = tmpS + s + "\n";
                    }
                    String finalTmpS = tmpS;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewNotification.setText(finalTmpS);
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}