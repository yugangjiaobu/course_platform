package com.example.backend.dao;

import com.example.backend.entity.CourseAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class CourseAssignmentDAOImpl implements CourseAssignmentDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseAssignmentDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCourseAssignment(CourseAssignment assignment) {
        String sql = "INSERT INTO courseassignments (course_id, title, description, deadline, created_by)" +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(),
                Timestamp.valueOf(assignment.getDeadline()), assignment.getCreatedBy());
    }

    public CourseAssignment getCourseAssignment(int assignmentId) {
        String sql = "SELECT * FROM courseassignments WHERE assignment_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{assignmentId}, (rs, rowNum) -> {
            return new CourseAssignment(
                    rs.getInt("assignment_id"),
                    rs.getString("course_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getTimestamp("deadline").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        });
    }

    public List<CourseAssignment> getAllCourseAssignments() {
        String sql = "SELECT * FROM courseassignments";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new CourseAssignment(
                    rs.getInt("assignment_id"),
                    rs.getString("course_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getTimestamp("deadline").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        });
    }

    public void updateCourseAssignment(CourseAssignment assignment) {
        String sql = "UPDATE courseassignments SET course_id = ?, title = ?, description = ?, deadline = ?," +
                "created_by = ? WHERE assignment_id = ?";
        jdbcTemplate.update(sql, assignment.getCourseId(), assignment.getTitle(), assignment.getDescription(),
                Timestamp.valueOf(assignment.getDeadline()), assignment.getCreatedBy(), assignment.getAssignmentId());
    }

    public void deleteCourseAssignment(int assignmentId) {
        String sql = "DELETE FROM courseassignments WHERE assignment_id = ?";
        jdbcTemplate.update(sql, assignmentId);
    }
}