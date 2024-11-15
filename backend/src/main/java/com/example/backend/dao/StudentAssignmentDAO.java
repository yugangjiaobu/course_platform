package com.example.backend.dao;

import com.example.backend.entity.StudentAssignment;

import java.sql.SQLException;
import java.util.List;

public interface StudentAssignmentDAO {
    void addStudentAssignment(StudentAssignment submission) throws SQLException;
    StudentAssignment getStudentAssignment(int submissionId) throws SQLException;
    List<StudentAssignment> getAllStudentAssignments() throws SQLException;
    void updateStudentAssignment(StudentAssignment submission) throws SQLException;
    void deleteStudentAssignment(int submissionId) throws SQLException;
}