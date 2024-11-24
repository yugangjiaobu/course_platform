package com.example.backend.dao;

import com.example.backend.entity.ExamRoom;

import java.util.List;

public interface ExamRoomDAO {
    void addExamRoom(ExamRoom examRoom) throws Exception;
    ExamRoom getExamRoomById(int roomId) throws Exception;
    List<ExamRoom> getAllExamRooms() throws Exception;
    void updateExamRoom(ExamRoom examRoom) throws Exception;
    void deleteExamRoom(int roomId) throws Exception;
}