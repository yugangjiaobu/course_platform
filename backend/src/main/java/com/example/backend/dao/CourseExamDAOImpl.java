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

    @Override
    public void save(CourseExam courseExam) throws SQLException {
        try {
            /*System.out.println(courseExam.getExamId());
            System.out.println(courseExam.getCourseId());
            System.out.println(courseExam.getExamDate());*/

            String sql = "INSERT INTO courseexams (exam_id, course_id, title, description, exam_date, created_by, created_at, updated_at, answer) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, courseExam.getExamId(), courseExam.getCourseId(), courseExam.getTitle(),
                    courseExam.getDescription(), courseExam.getExamDate(), courseExam.getCreatedBy(),
                    courseExam.getCreatedAt(), courseExam.getUpdatedAt(), courseExam.getAnswer());

            //System.out.println("Insert successful.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public CourseExam getById(String examId) throws SQLException {
        String sql = "SELECT * FROM courseexams WHERE exam_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{examId}, mapRow());
    }

    @Override
    public List<CourseExam> getExamsByCourseName(String courseName) throws SQLException {
        String sql = "SELECT * FROM courseexams WHERE course_id IN (SELECT course_id FROM courses WHERE course_name = ?)";
        return jdbcTemplate.query(sql, new Object[]{courseName}, (rs, rowNum) -> {
            CourseExam courseExam = new CourseExam();
            courseExam.setExamId(rs.getString("exam_id"));
            courseExam.setCourseId(rs.getString("course_id"));
            courseExam.setTitle(rs.getString("title"));
            courseExam.setDescription(rs.getString("description"));
            courseExam.setExamDate(rs.getTimestamp("exam_date"));
            courseExam.setCreatedBy(rs.getString("created_by"));
            courseExam.setCreatedAt(rs.getTimestamp("created_at"));
            courseExam.setUpdatedAt(rs.getTimestamp("updated_at"));
            courseExam.setAnswer(rs.getString("answer"));
            return courseExam;
        });
    }

    @Override
    public CourseExam getCourseExamById(String examId) throws SQLException {
        String sql = "SELECT * FROM courseexams WHERE exam_id = ?";

        List<CourseExam> exams = jdbcTemplate.query(sql, new Object[]{examId}, mapRow());

        if (exams.isEmpty()) {
            return null; // Or throw an exception if no result is found
        }

        return exams.get(0); // Return the first result
    }


    private static RowMapper<CourseExam> mapRow() {
        return new RowMapper<CourseExam>() {
            @Override
            public CourseExam mapRow(ResultSet rs, int rowNum) throws SQLException {
                CourseExam courseExam = new CourseExam();
                courseExam.setExamId(rs.getString("exam_id"));
                courseExam.setCourseId(rs.getString("course_id"));
                courseExam.setTitle(rs.getString("title"));
                courseExam.setDescription(rs.getString("description"));
                courseExam.setExamDate(rs.getTimestamp("exam_date"));
                courseExam.setCreatedBy(rs.getString("created_by"));
                courseExam.setCreatedAt(rs.getTimestamp("created_at"));
                courseExam.setUpdatedAt(rs.getTimestamp("updated_at"));
                courseExam.setAnswer(rs.getString("answer"));
                return courseExam;
            }
        };
    }
}
