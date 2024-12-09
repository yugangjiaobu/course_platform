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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Exampaper exampaper) throws SQLException {
        String sql = "INSERT INTO exampapers (paper_id, notbut_exam_id, paper_content, question_num, answer_choice, choice_A, choice_B, choice_C, choice_D, deadline, course_id, testname) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                exampaper.getPaperId(),
                exampaper.getNotbutExamId(),
                exampaper.getPaperContent(),
                exampaper.getQuestionNum(),
                exampaper.getAnswerChoice(),
                exampaper.getChoiceA(),
                exampaper.getChoiceB(),
                exampaper.getChoiceC(),
                exampaper.getChoiceD(),
                exampaper.getDeadline(),
                exampaper.getCourseId(),
                exampaper.getTestName());
    }

    @Override
    public List<Exampaper> findByExamId(String notbutExamId) throws SQLException {
        String sql = "SELECT * FROM exampapers WHERE notbut_exam_id = ?";
        return jdbcTemplate.query(sql, new Object[]{notbutExamId}, new ExampaperRowMapper());
    }

    @Override
    public List<Exampaper> findByCourseId(String courseId) throws SQLException {
        String sql = "SELECT * FROM exampapers WHERE course_id = ?";
        return jdbcTemplate.query(sql, new Object[]{courseId}, new ExampaperRowMapper());
    }

    @Override
    public List<Exampaper> findByCourseIdAndTestname(String courseId, String testname) throws SQLException {
        String sql = "SELECT * FROM exampapers WHERE course_id = ? AND testname = ?";

        return jdbcTemplate.query(sql, new Object[]{courseId, testname}, new ExampaperRowMapper());
    }

    @Override
    public List<Exampaper> findExampaperByCourseAndTestname(String courseId, String testname) {
        String sql = "SELECT * FROM exampapers WHERE course_id = ? AND testname = ?";
        return jdbcTemplate.query(sql, new Object[]{courseId, testname}, new ExampaperRowMapper());
    }

    @Override
    public String getNotbutExamIdByTestname(String testname) throws SQLException {
        String sql = "SELECT notbut_exam_id FROM exampapers WHERE testname = ?";
        try {
            List<String> result = jdbcTemplate.query(sql, new Object[]{testname}, (ResultSet rs, int rowNum) -> {
                return rs.getString("notbut_exam_id");
            });

            if (!result.isEmpty()) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }



    // RowMapper for Exampaper
    private static class ExampaperRowMapper implements RowMapper<Exampaper> {
        @Override
        public Exampaper mapRow(ResultSet rs, int rowNum) throws SQLException {
            Exampaper exampaper = new Exampaper();
            exampaper.setPaperId(rs.getString("paper_id"));
            exampaper.setNotbutExamId(rs.getString("notbut_exam_id"));
            exampaper.setPaperContent(rs.getString("paper_content"));
            exampaper.setQuestionNum(rs.getInt("question_num"));
            exampaper.setAnswerChoice(rs.getString("answer_choice"));
            exampaper.setChoiceA(rs.getString("choice_A"));
            exampaper.setChoiceB(rs.getString("choice_B"));
            exampaper.setChoiceC(rs.getString("choice_C"));
            exampaper.setChoiceD(rs.getString("choice_D"));
            exampaper.setDeadline(rs.getTimestamp("deadline"));
            exampaper.setCourseId(rs.getString("course_id"));
            exampaper.setTestName(rs.getString("testname"));
            return exampaper;
        }
    }
}
