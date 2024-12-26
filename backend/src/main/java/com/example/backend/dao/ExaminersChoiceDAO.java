package com.example.backend.dao;

import com.example.backend.entity.*;

import java.sql.SQLException;
import java.util.List;

public interface ExaminersChoiceDAO {
    List<ExaminersChoice> findByExamId(String notbutExamId) throws SQLException;
    void save(ExaminersChoice examinersChoice) throws SQLException;
    Integer getScoreByExamIdAndUserId(String notbutExamId, String userId) throws SQLException;
    void saveChoiceNumForStudent(String name, String exam_num, String notbutExamId,
                                 String userId, String stu_choice, int score, String choice_num) throws SQLException;
    void saveStudentAnswer(String notbutExamId, String userId, String stuChoice, int score);
}
