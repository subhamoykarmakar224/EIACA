package com.example.collectdata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.collectdata.bean.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityQuestions extends AppCompatActivity {

    private static final String TAG = "ActQuestions :" + "ollo";

    // Widgets
    RadioGroup radioGroup;
    TextView textViewQuestion;


    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        radioGroup = findViewById(R.id.radioGroupOptions);
        textViewQuestion = findViewById(R.id.txtViewQuestion);

        questionList = new ArrayList<>();
        // Get questions from the file
        getQuestionDetails();


    }

    private void getQuestionDetails(){
        String jsonText="";
        BufferedReader bufferefReader;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.questions);
            bufferefReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferefReader.readLine();



//            byte[] buffer = new byte[is.available()];
//            while (is.read(buffer) != -1) {
//                jsonText = new String(buffer);
//            }
//            String [] qsData = jsonText.split(";;");
//            Log.i(TAG, "Data----");
//            for(int i =0; i < qsData.length; i ++) {
////                questionList.add(new Question(qsData[i], qsData[i+1], qsData[i+2]));
//                Log.i(TAG, qsData[i] + " :: " + qsData[i+1] + " :: "+ qsData[i+2]);
//            }
        } catch(IOException e) {
            Log.i(TAG, "Problem reading file: " + e.getMessage());
        }
    }

    public void btnClickedLoadPrevQuestion(View view) {
    }

    public void btnClickedLoadNextQuestion(View view) {
    }
}