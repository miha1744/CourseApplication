package com.example.agnohendrix.androidonlinequizapp.Model;

public class QuestionScore {
    private String question_Score;
    private String user;
    private String score;
    private String categoryId;
    private String categoryName;
    private String categoryKol;


    public QuestionScore(){}

    public String getQuestion_Score() {
        return question_Score;
    }

    public void setQuestion_Score(String question_Score) {
        this.question_Score = question_Score;
    }

    public String getUser() {
        return user;
    }

    public String getCategoryKol() {
        return categoryKol;
    }

    public void setCategoryKol(String categoryKol) {
        this.categoryKol = categoryKol;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public QuestionScore(String question_Score, String user, String score, String categoryId, String categoryName,String categoryKol) {
        this.question_Score = question_Score;
        this.user = user;
        this.score = score;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.question_Score = question_Score;
        this.categoryKol = categoryKol;
    }
}
