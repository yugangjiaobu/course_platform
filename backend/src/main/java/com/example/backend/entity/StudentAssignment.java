package com.example.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StudentAssignment {
    private int submissionId;
    private int assignmentId;
    private String studentId;
    private String submissionContent;
    private LocalDateTime submissionTime;
    private BigDecimal grade;
    private String feedback;

    // Default constructor
    public StudentAssignment() {}

    // Constructor with parameters
    public StudentAssignment(int submissionId, int assignmentId, String studentId, String submissionContent,
                             LocalDateTime submissionTime, BigDecimal grade, String feedback) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.submissionContent = submissionContent;
        this.submissionTime = submissionTime;
        this.grade = grade;
        this.feedback = feedback;
    }

    // Getters and setters
    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubmissionContent() {
        return submissionContent;
    }

    public void setSubmissionContent(String submissionContent) {
        this.submissionContent = submissionContent;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}