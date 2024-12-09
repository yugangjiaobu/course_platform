package com.example.backend.dao;

import com.example.backend.entity.Examiner;

import java.sql.SQLException;

public interface ExaminerDAO {
    void save(Examiner examiner) throws SQLException;
    Examiner getById(String examNum) throws SQLException;
    Examiner getExaminerByExamIdAndUserId(String examId, String userId) throws SQLException;
}
