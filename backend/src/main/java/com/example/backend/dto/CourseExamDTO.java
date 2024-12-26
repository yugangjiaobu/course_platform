package com.example.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public class CourseExamDTO {
    private String coursename;
    private String title;
    private Timestamp time;
    private String notice;
    private String location;
    private int setstate; // 0 for ordered, 1 for random
    private MultipartFile file;

    public String getCourseName() {
        return coursename;
    }

    public void setCourseName(String courseName) {
        this.coursename = courseName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getExamDate() {
        return time;
    }

    public void setExamDate(Timestamp examDate) {
        this.time = examDate;
    }

    public String getDescription() {
        return notice;
    }

    public void setDescription(String description) {
        this.notice = description;
    }

    public String getRoomName() {
        return location;
    }

    public void setRoomName(String roomName) {
        this.location = roomName;
    }

    public int getSetState() {
        return setstate;
    }

    public void setSetState(int setState) {
        this.setstate = setState;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
