package com.example.backend.dao;

import com.example.backend.entity.ExamRoom;

import java.sql.SQLException;
import java.util.List;

public interface ExamRoomDAO {
    void save(ExamRoom examRoom) throws SQLException;
    ExamRoom getById(String roomId) throws SQLException;
    ExamRoom getByExamId(String examId) throws SQLException;
}
