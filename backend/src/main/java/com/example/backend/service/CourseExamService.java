package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CourseExamService {
    void setExam(CourseExamDTO courseExamDTO) throws Exception;
    List<CourseExamResponseDTO> getExams(String token, String courseName) throws Exception;
    File downloadExam(String examId) throws IOException, SQLException;
}