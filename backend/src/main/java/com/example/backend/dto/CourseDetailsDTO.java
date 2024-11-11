package com.example.backend.dto;

import com.example.backend.entity.User;

import java.util.List;

public class CourseDetailsDTO {
    private String courseName;
    private String courseDescription;
    private String teacherName;
    private String teacherEmail;
    private List<User> students;
    private List<String> syllabus;
    private boolean isTeacher;

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<String> getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(List<String> syllabus) {
        this.syllabus = syllabus;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }
}
