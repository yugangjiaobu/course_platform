package com.example.backend.dao;

import com.example.backend.entity.Exampaper;

import java.sql.SQLException;
import java.util.List;

public interface ExampaperDAO {
    void save(Exampaper exampaper) throws SQLException;
    List<Exampaper> findByExamId(String notbutExamId) throws SQLException;
    List<Exampaper> findByCourseId(String courseId) throws SQLException;
    List<Exampaper> findByCourseIdAndTestname(String courseId, String testname) throws SQLException;
    List<Exampaper> findExampaperByCourseAndTestname(String courseId, String testname);
    String getNotbutExamIdByTestname(String testname) throws SQLException;
}
