package com.example.collectdata;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collectdata.bean.Question;
import com.example.collectdata.services.network.SendDataToServer;
import com.example.collectdata.sharedpref.SharedPreferenceControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ActivityQuestions extends AppCompatActivity implements View.OnClickListener {

    // Lubben Scale Question:: https://www.brandeis.edu/roybal/docs/LSNS_website_PDF.pdf
    // https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6199213/

    private static final String TAG = "ActQuestions :" + "ollo";

    // Widgets
    RadioGroup radioGroup;
    TextView textViewQuestion;
    Button btnNext, btnPrev;

    List<Question> questionList;
    int questionCount = 0;

    int currentScore = 0;
    int [] scoreTracking;

    SharedPreferenceControl spControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        radioGroup = findViewById(R.id.radioGroupOptions);
        textViewQuestion = findViewById(R.id.txtViewQuestion);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        questionCount = 0;
        questionList = new ArrayList<>();

        spControl = new SharedPreferenceControl(this);

        // Get questions from the file
        getQuestionDetails();

        generateQuestionAndRadioButtons(0);
    }

    private void getQuestionDetails(){
        BufferedReader bufferefReader;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.questions_lsns_6);
            bufferefReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferefReader.readLine();
            Question question = new Question();
            int cnt = 1;
            while(line != null) {
                if(cnt == 1) question.setQuestion(line);
                if(cnt == 2) question.setOptions(line);
                if(cnt == 3) question.setScores(line);
                if(line.equalsIgnoreCase(";;")) {
                    questionList.add(question);
                    question = new Question();
                    cnt = 0;
                }
                cnt += 1;
                line = bufferefReader.readLine();
            }
        } catch(IOException e) {
            Log.i(TAG, "Problem reading file: " + e.getMessage());
        }
        scoreTracking = new int[questionList.size()];
        Arrays.fill(scoreTracking, -1);
    }

    public void btnClickedLoadPrevQuestion(View view) {
        if(questionCount == 0)
            return;
        btnNext.setText("Next");

        questionCount -= 1;
        generateQuestionAndRadioButtons(questionCount);
    }

    public void btnClickedLoadNextQuestion(View view) {
        if(btnNext.getText().equals("Submit")) {
            submitScores();
        }
        if(questionCount == questionList.size()-1) return;
        questionCount += 1;
        if(questionCount == questionList.size()-1)
            btnNext.setText("Submit");
        generateQuestionAndRadioButtons(questionCount);
    }

    private void generateQuestionAndRadioButtons(int c) {
        Question q = questionList.get(c);
        textViewQuestion.setText(q.getQuestion());

        String [] options = q.getOptions().split(";");
        String [] scores = q.getScores().split(";");
        radioGroup.removeAllViewsInLayout();
        for(int i = 0; i < options.length; i++) {
            RadioButton rBtn = new RadioButton(this);
            if(scoreTracking[questionCount] > 0 && i == scoreTracking[questionCount]-1) {
                rBtn.setChecked(true);
            }
            rBtn.setId(Integer.parseInt(scores[i]));
            rBtn.setText(options[i].trim());
            rBtn.setOnClickListener(this);
            rBtn.setPadding(20,20,20,20);
            rBtn.setTextSize(15);
            radioGroup.addView(rBtn);
        }
    }

    @Override
    public void onClick(View v) {
        scoreTracking[questionCount] = v.getId();
    }

    private void submitScores() {
        int s = 0;
        boolean answeredAllQs = true;
        for(int i : scoreTracking) {
            if(i == -1){
                answeredAllQs = false;
                break;
            }
            s += i;
        }
        Log.i(TAG, "Scores: " + s);
        if(!answeredAllQs){
            Toast.makeText(this, "You have not answered all questions. Please answer all questions to continue.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Score :: " + s, Toast.LENGTH_LONG).show();
            // TODO :: seperate out cases for other ranges of scores
            if(s <= 15) {
                (new SendDataToServer(this, constructPacket(1))).sendEmotionCode();
            }
            // TODO :: Uncomment later
            finish();
        }
    }

    private String constructPacket(int emotCode) {
        String packet = "{";
        packet = packet + "\"id\":" + "1,";
        packet = packet + "\"name\":\"" + spControl.getData(Constants.SP_KEY_MY_UNAME) + "\",";
        packet = packet + "\"phoneNumber\":\"" + "9876543214" + "\",";
        packet = packet + "\"dateTime\":\"" + String.valueOf(new Date()) + "\",";
        switch (emotCode) {
            case 1:
                packet = packet + "\"status\":\"" + "sad" + "\"";
                break;
        }
        packet = packet + "}";
        return packet;
    }
}