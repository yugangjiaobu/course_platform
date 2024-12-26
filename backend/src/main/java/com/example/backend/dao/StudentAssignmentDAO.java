package com.example.backend.dao;

import com.example.backend.entity.StudentAssignment;

import java.util.List;

public interface StudentAssignmentDAO {
    List<StudentAssignment> getSubmissionsByAssignment(String assignmentId);
    StudentAssignment getSubmissionByStudentAndAssignment(String studentId, String assignmentId);
    void saveSubmission(StudentAssignment submission);
    StudentAssignment getStudentAssignmentById(String assignmentId, String studentId);
    void updateScore(StudentAssignment studentAssignment);
}
