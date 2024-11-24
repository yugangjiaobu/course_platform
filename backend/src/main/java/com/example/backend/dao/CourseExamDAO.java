package com.example.backend.dao;

import com.example.backend.entity.CourseExam;

import java.sql.SQLException;
import java.util.List;

public interface CourseExamDAO {
    public void addCourseExam(CourseExam courseExam) throws SQLException;
    public CourseExam getCourseExamById(int examId) throws SQLException;
    public void updateCourseExam(CourseExam courseExam) throws SQLException;
    public void deleteCourseExam(int examId) throws SQLException;
    public List<CourseExam> getAllCourseExams() throws SQLException;
}