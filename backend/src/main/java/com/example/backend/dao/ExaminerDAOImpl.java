package com.example.backend.dao;

import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExaminerDAOImpl implements ExaminerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Examiner examiner) throws SQLException {

        /*System.out.println(examiner.getExamId());
        System.out.println(examiner.getName());
        System.out.println(examiner.getSeatNum());*/

        String sql = "INSERT INTO examiners (name, exam_num, seat_num, room_id, exam_id, user_id, stu_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, examiner.getName(), examiner.getExamNum(), examiner.getSeatNum(),
                    examiner.getRoomId(), examiner.getExamId(), examiner.getUserId(), examiner.getStuAnswer());
        } catch (Exception e) {
            System.err.println("Error occurred while saving the examiner: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public Examiner getById(String examNum) throws SQLException {
        String sql = "SELECT * FROM examiners WHERE exam_num = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{examNum}, mapRow());
    }

    @Override
    public Examiner getExaminerByExamIdAndUserId(String examId, String userId) throws SQLException {
        String sql = "SELECT * FROM examiners WHERE exam_id = ? AND user_id = ?";

        // 使用 query 查询，返回一个 List
        List<Examiner> examiners = jdbcTemplate.query(sql, new Object[]{examId, userId}, mapRow());

        // 如果结果列表为空，则返回 null
        if (examiners.isEmpty()) {
            return null;
        }

        // 返回查询结果中的第一条记录
        return examiners.get(0);
    }


    private static RowMapper<Examiner> mapRow() {
        return new RowMapper<Examiner>() {
            @Override
            public Examiner mapRow(ResultSet rs, int rowNum) throws SQLException {
                Examiner examiner = new Examiner();
                examiner.setName(rs.getString("name"));
                examiner.setExamNum(rs.getString("exam_num"));
                examiner.setSeatNum(rs.getInt("seat_num"));
                examiner.setRoomId(rs.getString("room_id"));
                examiner.setExamId(rs.getString("exam_id"));
                examiner.setUserId(rs.getString("user_id"));
                examiner.setStuAnswer(rs.getString("stu_answer"));
                return examiner;
            }
        };
    }
}
