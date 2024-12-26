package com.example.backend.dto;

import java.sql.Timestamp;

public class CourseExamResponseDTO {
    private String examid;
    private String examName;
    private String location;
    private Timestamp time;
    private String notice;
    private String url;
    private String seatId;

    public String getExamId() {
        return examid;
    }

    public void setExamId(String examId) {
        this.examid = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }
}
