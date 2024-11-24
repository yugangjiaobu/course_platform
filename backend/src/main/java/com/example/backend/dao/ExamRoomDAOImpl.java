package com.example.backend.dao;

import com.example.backend.entity.ExamRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExamRoomDAOImpl implements ExamRoomDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addExamRoom(ExamRoom examRoom) {
        String sql = "INSERT INTO examrooms (exam_id, room_name, seating_order) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, examRoom.getExamId(), examRoom.getRoomName(), examRoom.getSeatingOrder());
    }

    @Override
    public ExamRoom getExamRoomById(int roomId) {
        String sql = "SELECT * FROM examrooms WHERE room_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{roomId}, new ExamRoomRowMapper());
    }

    @Override
    public List<ExamRoom> getAllExamRooms() {
        String sql = "SELECT * FROM examrooms";
        return jdbcTemplate.query(sql, new ExamRoomRowMapper());
    }

    @Override
    public void updateExamRoom(ExamRoom examRoom) {
        String sql = "UPDATE examrooms SET exam_id = ?, room_name = ?, seating_order = ? WHERE room_id = ?";
        jdbcTemplate.update(sql, examRoom.getExamId(), examRoom.getRoomName(), examRoom.getSeatingOrder(), examRoom.getRoomId());
    }

    @Override
    public void deleteExamRoom(int roomId) {
        String sql = "DELETE FROM examrooms WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    private static final class ExamRoomRowMapper implements RowMapper<ExamRoom> {
        @Override
        public ExamRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExamRoom examRoom = new ExamRoom();
            examRoom.setRoomId(rs.getInt("room_id"));
            examRoom.setExamId(rs.getInt("exam_id"));
            examRoom.setRoomName(rs.getString("room_name"));
            examRoom.setSeatingOrder(rs.getString("seating_order"));
            return examRoom;
        }
    }
}