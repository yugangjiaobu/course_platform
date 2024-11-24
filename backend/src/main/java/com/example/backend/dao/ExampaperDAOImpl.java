package com.example.backend.dao;

import com.example.backend.entity.Exampaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExampaperDAOImpl implements ExampaperDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExampaperDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create
    public void addExampaper(Exampaper exampaper) {
        String query = "INSERT INTO exampapers (exam_id, paper_content, uploaded_by) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, exampaper.getExamId(), exampaper.getPaperContent(), exampaper.getUploadedBy());
    }

    // Read
    public Exampaper getExampaperById(int paperId) {
        String query = "SELECT * FROM exampapers WHERE paper_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{paperId}, new ExampaperRowMapper());
    }

    // Update
    public void updateExampaper(Exampaper exampaper) {
        String query = "UPDATE exampapers SET exam_id = ?, paper_content = ?, uploaded_by = ? WHERE paper_id = ?";
        jdbcTemplate.update(query, exampaper.getExamId(), exampaper.getPaperContent(), exampaper.getUploadedBy(), exampaper.getPaperId());
    }

    // Delete
    public void deleteExampaper(int paperId) {
        String query = "DELETE FROM exampapers WHERE paper_id = ?";
        jdbcTemplate.update(query, paperId);
    }

    // List all
    public List<Exampaper> getAllExampapers() {
        String query = "SELECT * FROM exampapers";
        return jdbcTemplate.query(query, new ExampaperRowMapper());
    }

    // RowMapper for Exampaper
    private static class ExampaperRowMapper implements RowMapper<Exampaper> {
        @Override
        public Exampaper mapRow(ResultSet rs, int rowNum) throws SQLException {
            Exampaper exampaper = new Exampaper();
            exampaper.setPaperId(rs.getInt("paper_id"));
            exampaper.setExamId(rs.getInt("exam_id"));
            exampaper.setPaperContent(rs.getString("paper_content"));
            exampaper.setUploadedBy(rs.getString("uploaded_by"));
            exampaper.setUploadedAt(rs.getTimestamp("uploaded_at"));
            return exampaper;
        }
    }
}