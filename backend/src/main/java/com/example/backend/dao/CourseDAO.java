package com.example.backend.dao;

import com.example.backend.entity.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDAO {
    void addCourse(Course course) throws SQLException;
    Course getCourseById(String courseId) throws SQLException;
    List<Course> getAllCourses() throws SQLException;
    void updateCourse(Course course) throws SQLException;
    void deleteCourse(String courseId) throws SQLException;

    Course getCourseByName(String courseName) throws SQLException;
    User getTeacherByCourseId(String courseId) throws SQLException;
    List<User> getStudentsByCourseId(String courseId) throws SQLException;
}
