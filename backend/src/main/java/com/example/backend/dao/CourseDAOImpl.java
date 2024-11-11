package com.example.backend.dao;

import com.example.backend.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses (course_id, course_name, course_description, teacher_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, course.getCourseId(), course.getCourseName(), course.getCourseDescription(), course.getTeacherId());
    }

    public Course getCourseById(String courseId) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{courseId}, new CourseRowMapper());
    }

    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT * FROM courses";
        return jdbcTemplate.query(query, new CourseRowMapper());
    }

    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, course_description = ?, teacher_id = ? WHERE course_id = ?";
        jdbcTemplate.update(query, course.getCourseName(), course.getCourseDescription(), course.getTeacherId(), course.getCourseId());
    }

    public void deleteCourse(String courseId) throws SQLException {
        String query = "DELETE FROM courses WHERE course_id = ?";
        jdbcTemplate.update(query, courseId);
    }

    private static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getString("course_description"),
                    rs.getString("teacher_id")
            );
        }
    }

    @Override
    public Course getCourseByName(String courseName) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_name = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{courseName}, new CourseRowMapper());
    }

    @Override
    public User getTeacherByCourseId(String courseId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = (SELECT teacher_id FROM courses WHERE course_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{courseId}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        });
    }

    @Override
    public List<User> getStudentsByCourseId(String courseId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id IN (SELECT user_id FROM user_courses WHERE course_id = ?)";
        return jdbcTemplate.query(query, new Object[]{courseId}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        });
    }
}
