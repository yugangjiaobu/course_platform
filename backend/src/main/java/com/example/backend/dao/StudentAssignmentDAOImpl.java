package com.example.backend.dao;

import com.example.backend.entity.StudentAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentAssignmentDAOImpl implements StudentAssignmentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addStudentAssignment(StudentAssignment submission) {
        String sql = "INSERT INTO studentassignments (assignment_id, student_id, submission_content) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, submission.getAssignmentId(), submission.getStudentId(),
                submission.getSubmissionContent());
    }

    public StudentAssignment getStudentAssignment(int submissionId) {
        String sql = "SELECT * FROM studentassignments WHERE submission_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{submissionId}, new StudentAssignmentRowMapper());
    }

    public List<StudentAssignment> getAllStudentAssignments() {
        String sql = "SELECT * FROM studentassignments";
        return jdbcTemplate.query(sql, new StudentAssignmentRowMapper());
    }

    public void updateStudentAssignment(StudentAssignment submission) {
        String sql = "UPDATE studentassignments SET submission_content = ?, grade = ?, feedback = ?" +
                "WHERE submission_id = ?";
        jdbcTemplate.update(sql, submission.getSubmissionContent(), submission.getGrade(), submission.getFeedback(),
                submission.getSubmissionId());
    }

    public void deleteStudentAssignment(int submissionId) {
        String sql = "DELETE FROM studentassignments WHERE submission_id = ?";
        jdbcTemplate.update(sql, submissionId);
    }

    private static class StudentAssignmentRowMapper implements RowMapper<StudentAssignment> {
        @Override
        public StudentAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StudentAssignment(
                    rs.getInt("submission_id"),
                    rs.getInt("assignment_id"),
                    rs.getString("student_id"),
                    rs.getString("submission_content"),
                    rs.getTimestamp("submission_time").toLocalDateTime(),
                    rs.getBigDecimal("grade"),
                    rs.getString("feedback")
            );
        }
    }
}