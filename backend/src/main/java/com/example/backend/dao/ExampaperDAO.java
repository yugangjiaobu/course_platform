package com.example.backend.dao;

import com.example.backend.entity.Exampaper;

import java.sql.SQLException;
import java.util.List;

public interface ExampaperDAO {
    public void addExampaper(Exampaper exampaper) throws SQLException;
    public Exampaper getExampaperById(int paperId) throws SQLException;
    public void updateExampaper(Exampaper exampaper) throws SQLException;
    public void deleteExampaper(int paperId) throws SQLException;
    public List<Exampaper> getAllExampapers() throws SQLException;
}