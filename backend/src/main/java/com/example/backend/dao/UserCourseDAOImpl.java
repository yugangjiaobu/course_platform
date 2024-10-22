package com.example.backend.dao;

import com.example.backend.entity.UserCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserCourseDAOImpl implements UserCourseDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserCourseDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void enrollUserInCourse(UserCourse userCourse) throws SQLException {
        String query = "INSERT INTO user_courses (user_id, course_id, enrolled_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userCourse.getUserId(), userCourse.getCourseId(), userCourse.getEnrolledAt());
    }

    public UserCourse getUserCourse(String userId, String courseId) throws SQLException {
        String query = "SELECT * FROM user_courses WHERE user_id = ? AND course_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{userId, courseId}, (rs, rowNum) -> {
            return new UserCourse(
                    rs.getString("user_id"),
                    rs.getString("course_id"),
                    rs.getTimestamp("enrolled_at")
            );
        });
    }

    public List<UserCourse> getAllUserCourses(String userId) throws SQLException {
        String query = "SELECT * FROM user_courses WHERE user_id = ?";
        return jdbcTemplate.query(query, new Object[]{userId}, (rs, rowNum) -> {
            return new UserCourse(
                    rs.getString("user_id"),
                    rs.getString("course_id"),
                    rs.getTimestamp("enrolled_at")
            );
        });
    }

    public void deleteUserCourse(String userId, String courseId) throws SQLException {
        String query = "DELETE FROM user_courses WHERE user_id = ? AND course_id = ?";
        jdbcTemplate.update(query, userId, courseId);
    }
}
