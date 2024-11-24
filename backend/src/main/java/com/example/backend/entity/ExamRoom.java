package com.example.backend.entity;

public class ExamRoom {
    private int roomId;
    private int examId;
    private String roomName;
    private String seatingOrder;

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSeatingOrder() {
        return seatingOrder;
    }

    public void setSeatingOrder(String seatingOrder) {
        this.seatingOrder = seatingOrder;
    }
}