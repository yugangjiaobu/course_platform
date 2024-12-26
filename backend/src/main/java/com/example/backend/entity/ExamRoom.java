package com.example.backend.entity;

public class ExamRoom {
    private String roomId;
    private String examId;
    private String roomName;
    private String seatingOrder;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
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
