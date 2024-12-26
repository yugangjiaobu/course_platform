package com.example.backend.entity;

public class ExaminersChoice {
    private String name;
    private String examNum;
    private String notbutExamId;
    private String userId;
    private String stuChoice;
    private Integer score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamNum() {
        return examNum;
    }

    public void setExamNum(String examNum) {
        this.examNum = examNum;
    }

    public String getNotbutExamId() {
        return notbutExamId;
    }

    public void setNotbutExamId(String notbutExamId) {
        this.notbutExamId = notbutExamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStuChoice() {
        return stuChoice;
    }

    public void setStuChoice(String stuChoice) {
        this.stuChoice = stuChoice;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
