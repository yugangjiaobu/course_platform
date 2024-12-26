package com.example.backend.service;

import com.example.backend.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public interface CourseAssignmentService {
    void createAssignment(String courseName, String title, String description, String dueDate, MultipartFile file) throws Exception;
    File downloadAssignmentAttachment(String assignmentId) throws FileNotFoundException, SQLException;
    void uploadAssignment(String studentId, String assignmentId, MultipartFile file) throws Exception;
    File downloadStudentAssignment(String assignmentId, String studentId) throws FileNotFoundException, SQLException;
    void submitScore(ScoreSubmissionDTO scoreSubmissionDTO) throws Exception;
    List<CourseAssignmentDTO> getAssignments(String course, String token) throws Exception;
}
