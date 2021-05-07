package com.example.collectdata.bean;

public class Question {
    private String question;
    private String options;
    private String scores;

    public String getQuestion() {
        return question;
    }

    public Question() {
    }

    public Question(String question, String options, String scores) {
        this.question = question;
        this.options = options;
        this.scores = scores;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", options='" + options + '\'' +
                ", scores='" + scores + '\'' +
                '}';
    }
}
