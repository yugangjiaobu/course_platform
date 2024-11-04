package com.example.backend.dto;

public class CourseListDTO {
    private String courseName;

    public CourseListDTO() {}

    public CourseListDTO(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseListName() {
        return courseName;
    }

    public void setCourseListName(String courseName) {
        this.courseName = courseName;
    }
}