package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.*;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class ExampaperController {

    @Autowired
    private ExampaperService exampaperService;

    @PostMapping("/settest")
    public ResponseEntity<String> setTest(@RequestHeader("Authorization") String token,
                                          @RequestBody SetTestRequest request) {
        try {

            System.out.println(request.getDeadline());

            String jwtToken = extractJwtToken(token);
            String userRole = JWTUtil.extractRole(jwtToken);
            if (!"teacher".equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only teachers can set tests.");
            }

            exampaperService.createExampaperTest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Test set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error setting test: " + e.getMessage());
        }
    }

    @GetMapping("/gettests")
    public ResponseEntity<List<TestResponseDTO>> getTests(@RequestParam String coursename,
                                                          @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = extractJwtToken(token);
            String userRole = JWTUtil.extractRole(jwtToken);
            String userId = JWTUtil.extractUserID(jwtToken);
            List<TestResponseDTO> testList = exampaperService.getTestList(coursename, userRole, userId);
            return ResponseEntity.ok(testList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/gettestdetail")
    public ResponseEntity<List<ExampaperDetailDTO>> getTestDetails(
            @RequestParam String coursename,
            @RequestParam String testname,
            @RequestHeader("Authorization") String token) throws Exception {

        String jwtToken = extractJwtToken(token);
        String user_id = JWTUtil.extractUserID(jwtToken);

        List<ExampaperDetailDTO> testDetails = exampaperService.getTestDetails(coursename, testname, user_id);

        return ResponseEntity.ok(testDetails);
    }

    @PostMapping("/uploadtest")
    public void uploadTestAnswers(@RequestHeader("Authorization") String token,
                                  @RequestBody UploadTestAnswerDTO uploadTestAnswerDTO) throws Exception {

        //System.out.println(uploadTestAnswerDTO.getTestname());

        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);

        exampaperService.uploadTestAnswers(uploadTestAnswerDTO.getCoursename(), uploadTestAnswerDTO.getTestname(),
                uploadTestAnswerDTO.getAnswers(), userId);
    }

    // 提取 JWT token
    private String extractJwtToken(String authHeader) {
        String[] parts = authHeader.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            return null;
        }
        return parts[1];
    }
}