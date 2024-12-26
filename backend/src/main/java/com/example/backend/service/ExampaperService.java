package com.example.backend.service;

import com.example.backend.dto.*;

import java.sql.SQLException;
import java.util.List;

public interface ExampaperService {
    void createExampaperTest(SetTestRequest request) throws SQLException;
    List<TestResponseDTO> getTestList(String courseName, String userRole, String userId) throws SQLException;
    List<ExampaperDetailDTO> getTestDetails(String coursename, String testname, String user_id) throws Exception;
    void uploadTestAnswers(String coursename, String testname, List<UploadTestAnswerDTO.AnswerDTO> answers, String userId) throws Exception;
}
