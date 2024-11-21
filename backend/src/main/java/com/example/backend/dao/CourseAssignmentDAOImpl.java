package com.example.backend.dao;

import com.example.backend.entity.CourseAssignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseAssignmentDAOImpl implements CourseAssignmentDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addAssignment(CourseAssignment assignment) {
        String sql = "INSERT INTO courseassignments (assignment_id, course_id, title, description, deadline, created_by, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                assignment.getAssignmentId(),
                assignment.getCourseId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDeadline(),
                assignment.getCreatedBy(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt());
    }

    @Override
    public CourseAssignment getAssignmentById(String assignmentId) {
        String sql = "SELECT * FROM courseassignments WHERE assignment_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{assignmentId}, new AssignmentRowMapper());
    }

    @Override
    public List<CourseAssignment> getAssignmentsByCourseId(String courseId) {
        String sql = "SELECT * FROM courseassignments WHERE course_id = ?";
        return jdbcTemplate.query(sql, new Object[]{courseId}, new AssignmentRowMapper());
    }

    private static class AssignmentRowMapper implements RowMapper<CourseAssignment> {
        @Override
        public CourseAssignment mapRow(ResultSet rs, int rowNum) throws SQLException, SQLException {
            CourseAssignment assignment = new CourseAssignment();
            assignment.setAssignmentId(rs.getString("assignment_id"));
            assignment.setCourseId(rs.getString("course_id"));
            assignment.setTitle(rs.getString("title"));
            assignment.setDescription(rs.getString("description"));
            assignment.setDeadline(rs.getTimestamp("deadline"));
            assignment.setCreatedBy(rs.getString("created_by"));
            assignment.setCreatedAt(rs.getTimestamp("created_at"));
            assignment.setUpdatedAt(rs.getTimestamp("updated_at"));
            return assignment;
        }
    }
}
