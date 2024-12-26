package com.example.backend.dao;

import com.example.backend.entity.UserCourse;

import java.sql.SQLException;
import java.util.List;

public interface UserCourseDAO {
    void enrollUserInCourse(UserCourse userCourse) throws SQLException;
    UserCourse getUserCourse(String userId, String courseId) throws SQLException;
    List<UserCourse> getAllUserCourses(String userId) throws SQLException;
    void deleteUserCourse(String userId, String courseId) throws SQLException;
    List<UserCourse> getAllStudentsByCourseId(String courseId) throws SQLException;
}
