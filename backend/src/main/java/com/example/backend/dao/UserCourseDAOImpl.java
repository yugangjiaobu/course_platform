package com.example.backend.dao;

import com.example.backend.entity.UserCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserCourseDAOImpl implements UserCourseDAO {
    private Connection connection;

    public UserCourseDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public void enrollUserInCourse(UserCourse userCourse) throws SQLException {
        String query = "INSERT INTO user_courses (user_id, course_id, enrolled_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userCourse.getUserId());
            stmt.setString(2, userCourse.getCourseId());
            stmt.setTimestamp(3, userCourse.getEnrolledAt());
            stmt.executeUpdate();
        }
    }

    public UserCourse getUserCourse(String userId, String courseId) throws SQLException {
        String query = "SELECT * FROM user_courses WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserCourse(
                        rs.getString("user_id"),
                        rs.getString("course_id"),
                        rs.getTimestamp("enrolled_at")
                );
            }
        }
        return null;
    }

    public List<UserCourse> getAllUserCourses(String userId) throws SQLException {
        String query = "SELECT * FROM user_courses WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<UserCourse> userCourses = new ArrayList<>();
            while (rs.next()) {
                userCourses.add(new UserCourse(
                        rs.getString("user_id"),
                        rs.getString("course_id"),
                        rs.getTimestamp("enrolled_at")
                ));
            }
            return userCourses;
        }
    }

    public void deleteUserCourse(String userId, String courseId) throws SQLException {
        String query = "DELETE FROM user_courses WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, courseId);
            stmt.executeUpdate();
        }
    }
}