package com.example.backend.entity;

import java.sql.Timestamp;

public class UserCourse {
    private String userId;
    private String courseId;
    private Timestamp enrolledAt;

    // Constructors, getters, and setters
    public UserCourse() {}

    public UserCourse(String userId, String courseId, Timestamp enrolledAt) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}