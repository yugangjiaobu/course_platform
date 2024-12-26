package com.example.backend.dao;

import com.example.backend.entity.CourseExam;

import java.sql.SQLException;
import java.util.List;

public interface CourseExamDAO {
    void save(CourseExam courseExam) throws SQLException;
    CourseExam getById(String examId) throws SQLException;
    List<CourseExam> getExamsByCourseName(String courseName) throws SQLException;
    CourseExam getCourseExamById(String examId) throws SQLException;
}
