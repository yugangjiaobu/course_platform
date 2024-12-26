package com.example.backend.dao;

import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExaminersChoiceDAOImpl implements ExaminersChoiceDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ExaminersChoice> findByExamId(String notbutExamId) throws SQLException {
        String sql = "SELECT * FROM examiners_choice WHERE notbut_exam_id = ?";
        return jdbcTemplate.query(sql, new RowMapper<ExaminersChoice>() {
            @Override
            public ExaminersChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
                ExaminersChoice choice = new ExaminersChoice();
                choice.setName(rs.getString("name"));
                choice.setExamNum(rs.getString("exam_num"));
                choice.setNotbutExamId(rs.getString("notbut_exam_id"));
                choice.setUserId(rs.getString("user_id"));
                choice.setStuChoice(rs.getString("stu_choice"));
                choice.setScore(rs.getInt("score"));
                return choice;
            }
        }, notbutExamId);
    }

    @Override
    public void save(ExaminersChoice examinersChoice) throws SQLException {
        String sql = "INSERT INTO examiners_choice (name, exam_num, notbut_exam_id, user_id, stu_choice, score) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, examinersChoice.getName(), examinersChoice.getExamNum(),
                examinersChoice.getNotbutExamId(), examinersChoice.getUserId(), examinersChoice.getStuChoice(),
                examinersChoice.getScore());
    }

    @Override
    public Integer getScoreByExamIdAndUserId(String notbutExamId, String userId) throws SQLException {
        String sql = "SELECT score FROM examiners_choice WHERE notbut_exam_id = ? AND user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{notbutExamId, userId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void saveChoiceNumForStudent(String name, String exam_num, String notbutExamId, String userId,
                                        String stu_choice, int score, String choice_num) {

        String sql = "INSERT INTO examiners_choice (name, exam_num, notbut_exam_id, user_id, stu_choice, score, choice_num) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, name, exam_num, notbutExamId, userId, stu_choice, score, choice_num);
    }

    @Override
    public void saveStudentAnswer(String notbutExamId, String userId, String stuChoice, int score) {
        // 检查是否存在已有记录
        String checkSql = "SELECT count(*) FROM examiners_choice WHERE notbut_exam_id = ? AND user_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{notbutExamId, userId}, Integer.class);

        if (count > 0) {
            // 如果记录已存在，更新 stu_choice 和 score 字段
            String updateSql = "UPDATE examiners_choice SET stu_choice = ?, score = ? WHERE notbut_exam_id = ? AND user_id = ?";
            jdbcTemplate.update(updateSql, stuChoice, score, notbutExamId, userId);
        } else {
            // 如果没有记录，插入新的数据
            String insertSql = "INSERT INTO examiners_choice (notbut_exam_id, user_id, stu_choice, score) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, notbutExamId, userId, stuChoice, score);
        }
    }


}
