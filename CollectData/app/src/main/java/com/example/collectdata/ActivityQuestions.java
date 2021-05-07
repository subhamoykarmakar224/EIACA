package com.example.collectdata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.collectdata.bean.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityQuestions extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActQuestions :" + "ollo";

    // Widgets
    RadioGroup radioGroup;
    TextView textViewQuestion;
    Button btnNext, btnPrev;

    List<Question> questionList;
    int questionCount = 0;

    int currentScore = 0;
    int [] scoreTracking;

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

        // Get questions from the file
        getQuestionDetails();

        generateQuestionAndRadioButtons(0);
    }

    private void getQuestionDetails(){
        String jsonText="";
        BufferedReader bufferefReader;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.questions);
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
        Log.i(TAG, "Selected OPT: " + currentScore);
    }

    private void submitScores() {
        String s = "";
        for(int i : scoreTracking) {
            s += String.valueOf(i);
        }
        Log.i(TAG, "Scores: " + s);

        // TODO :: Uncomment later
        finish();
    }
}