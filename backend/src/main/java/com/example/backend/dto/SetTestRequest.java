package com.example.backend.dto;

import java.sql.Timestamp;
import java.util.List;

public class SetTestRequest {
    private String coursename;
    private String testname;
    private Timestamp deadline;
    private List<Choice> choices;

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

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

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private String title;
        private List<String> options;
        private int right;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }
    }

}
