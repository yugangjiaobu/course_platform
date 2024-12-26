package com.example.backend.dao;

import com.example.backend.entity.StudentAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Override
    public List<StudentAssignment> getSubmissionsByAssignment(String assignmentId) {
        String sql = "SELECT * FROM studentassignments WHERE assignment_id = ?";
        return jdbcTemplate.query(sql, new Object[]{assignmentId}, new StudentAssignmentRowMapper());
    }

    @Override
    public void saveSubmission(StudentAssignment submission) {
        String sql = "INSERT INTO studentassignments (assignment_id, student_id, submission_content, submission_time) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                submission.getAssignmentId(),
                submission.getStudentId(),
                submission.getSubmissionContent(),
                submission.getSubmissionTime());
    }

    @Override
    public StudentAssignment getSubmissionByStudentAndAssignment(String studentId, String assignmentId) {
        String sql = "SELECT * FROM studentassignments WHERE student_id = ? AND assignment_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{studentId, assignmentId}, new StudentAssignmentRowMapper());
    }

    @Override
    public StudentAssignment getStudentAssignmentById(String assignmentId, String studentId) {
        String sql = "SELECT * FROM studentassignments WHERE assignment_id = ? AND student_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{assignmentId, studentId}, new StudentAssignmentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; //
        }
    }


    @Override
    public void updateScore(StudentAssignment studentAssignment) {
        String sql = "UPDATE studentassignments SET grade = ? WHERE submission_id = ?";
        jdbcTemplate.update(sql, studentAssignment.getGrade(), studentAssignment.getSubmissionId());
    }

    private static class StudentAssignmentRowMapper implements RowMapper<StudentAssignment> {
        @Override
        public StudentAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            StudentAssignment submission = new StudentAssignment();
            submission.setSubmissionId(rs.getInt("submission_id"));
            submission.setAssignmentId(rs.getString("assignment_id"));
            submission.setStudentId(rs.getString("student_id"));
            submission.setSubmissionContent(rs.getString("submission_content"));
            submission.setSubmissionTime(rs.getTimestamp("submission_time"));
            submission.setGrade(rs.getDouble("grade"));
            submission.setFeedback(rs.getString("feedback"));

            return submission;
        }
    }
}
