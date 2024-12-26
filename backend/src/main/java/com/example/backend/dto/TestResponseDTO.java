package com.example.backend.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TestResponseDTO {

    private String testname;         // 测验名称
    private Timestamp deadline;      // 截止时间
    private Integer score;           // 学生的分数
    private List<StudentScore> studentScores = new ArrayList<>();  // 学生成绩列表
    private List<Question> questions = new ArrayList<>();  // 存储与每个 testname 相关的多个选择题

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<StudentScore> getStudentScores() {
        return studentScores;
    }

    public void setStudentScores(List<StudentScore> studentScores) {
        this.studentScores = studentScores;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    // 每个选择题的内容（问题和选项）
    public static class Question {
        private String paperId;  // 选择题唯一标识
        private String choiceA;  // 选项 A
        private String choiceB;  // 选项 B
        private String choiceC;  // 选项 C
        private String choiceD;  // 选项 D

        public String getPaperId() {
            return paperId;
        }

        public void setPaperId(String paperId) {
            this.paperId = paperId;
        }

        public String getChoiceA() {
            return choiceA;
        }

        public void setChoiceA(String choiceA) {
            this.choiceA = choiceA;
        }

        public String getChoiceB() {
            return choiceB;
        }

        public void setChoiceB(String choiceB) {
            this.choiceB = choiceB;
        }

        public String getChoiceC() {
            return choiceC;
        }

        public void setChoiceC(String choiceC) {
            this.choiceC = choiceC;
        }

        public String getChoiceD() {
            return choiceD;
        }

        public void setChoiceD(String choiceD) {
            this.choiceD = choiceD;
        }
    }

    // 学生成绩内部类
    public static class StudentScore {
        private String name;  // 学生姓名
        private Integer score;  // 学生分数

        public StudentScore(String name, Integer score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }
    }
}
