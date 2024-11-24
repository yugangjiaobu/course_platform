package com.example.backend.dao;

import com.example.backend.entity.CourseExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseExamDAOImpl implements CourseExamDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create
    public void addCourseExam(CourseExam courseExam) {
        String sql = "INSERT INTO courseexams (course_id, title, description, exam_date, created_by) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, courseExam.getCourseId(), courseExam.getTitle(), courseExam.getDescription(), courseExam.getExamDate(), courseExam.getCreatedBy());
    }

    // Read
    public CourseExam getCourseExamById(int examId) {
        String sql = "SELECT * FROM courseexams WHERE exam_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{examId}, new CourseExamRowMapper());
    }

    // Update
    public void updateCourseExam(CourseExam courseExam) {
        String sql = "UPDATE courseexams SET course_id = ?, title = ?, description = ?, exam_date = ?, created_by = ? WHERE exam_id = ?";
        jdbcTemplate.update(sql, courseExam.getCourseId(), courseExam.getTitle(), courseExam.getDescription(), courseExam.getExamDate(), courseExam.getCreatedBy(), courseExam.getExamId());
    }

    // Delete
    public void deleteCourseExam(int examId) {
        String sql = "DELETE FROM courseexams WHERE exam_id = ?";
        jdbcTemplate.update(sql, examId);
    }

    // List
    public List<CourseExam> getAllCourseExams() {
        String sql = "SELECT * FROM courseexams";
        return jdbcTemplate.query(sql, new CourseExamRowMapper());
    }

    // RowMapper for CourseExam
    private static final class CourseExamRowMapper implements RowMapper<CourseExam> {
        @Override
        public CourseExam mapRow(ResultSet rs, int rowNum) throws SQLException {
            CourseExam courseExam = new CourseExam();
            courseExam.setExamId(rs.getInt("exam_id"));
            courseExam.setCourseId(rs.getString("course_id"));
            courseExam.setTitle(rs.getString("title"));
            courseExam.setDescription(rs.getString("description"));
            courseExam.setExamDate(rs.getTimestamp("exam_date"));
            courseExam.setCreatedBy(rs.getString("created_by"));
            courseExam.setCreatedAt(rs.getTimestamp("created_at"));
            courseExam.setUpdatedAt(rs.getTimestamp("updated_at"));
            return courseExam;
        }
    }
}