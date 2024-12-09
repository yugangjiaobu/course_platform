package com.example.backend.dao;

import com.example.backend.entity.ExamRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExamRoomDAOImpl implements ExamRoomDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(ExamRoom examRoom) throws SQLException {

        //System.out.println(examRoom.getRoomName());

        String sql = "INSERT INTO examrooms (room_id, exam_id, room_name, seating_order) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, examRoom.getRoomId(), examRoom.getExamId(), examRoom.getRoomName(), examRoom.getSeatingOrder());
        } catch (Exception e) {

            // 可以附加更多的上下文信息
            throw new SQLException("Failed to save ExamRoom with roomId: " + examRoom.getRoomId() + ". Error: " + e.getMessage(), e);
        }
    }


    @Override
    public ExamRoom getById(String roomId) throws SQLException {
        String sql = "SELECT * FROM examrooms WHERE room_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{roomId}, mapRow());
    }

    @Override
    public ExamRoom getByExamId(String examId) throws SQLException {
        String sql = "SELECT * FROM examrooms WHERE exam_id = ?";
        // 查询所有匹配的记录
        List<ExamRoom> result = jdbcTemplate.query(sql, new Object[]{examId}, mapRow());

        // 使用 DataAccessUtils 处理结果为空的情况
        return DataAccessUtils.singleResult(result); // 如果结果为空，返回 null
    }

    private static RowMapper<ExamRoom> mapRow() {
        return new RowMapper<ExamRoom>() {
            @Override
            public ExamRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
                ExamRoom examRoom = new ExamRoom();
                examRoom.setRoomId(rs.getString("room_id"));
                examRoom.setExamId(rs.getString("exam_id"));
                examRoom.setRoomName(rs.getString("room_name"));
                examRoom.setSeatingOrder(rs.getString("seating_order"));
                return examRoom;
            }
        };
    }
}
