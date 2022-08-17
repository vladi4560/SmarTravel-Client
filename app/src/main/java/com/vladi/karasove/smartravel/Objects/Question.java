package com.vladi.karasove.smartravel.Objects;

public class Question {
    private int questionNumber;
    private String questionRate;
    private int answerRate=-1;

    public Question(int qaNumber, String questionRate) {
        this.questionNumber = qaNumber;
        this.questionRate = questionRate;
    }

    public String getQuestionRate() {
        return questionRate;
    }

    public int getAnswerRate() {
        return answerRate;
    }

    public Question setAnswerRate(int answerRate) {
        this.answerRate = answerRate;
        return this;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }
}