package com.example.backend.controller;

import com.example.backend.dto.CourseAssignmentDTO;
import com.example.backend.dto.ScoreSubmissionDTO;
import com.example.backend.service.CourseAssignmentService;
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
import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081") // 替换为你的前端地址
public class CourseAssignmentController {
    @Autowired
    private CourseAssignmentService courseAssignmentService;

    //教师上传作业
    @PostMapping("/creatassignment")
    public ResponseEntity<String> createAssignment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("course") String courseName,
            @RequestParam("description") String description,
            @RequestParam("title") String title,
            @RequestParam("dueDate") String dueDate) {
        try {
            courseAssignmentService.createAssignment(courseName, title, description, dueDate, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("作业创建成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("作业创建失败: " + e.getMessage());
        }
    }

    // 学生下载老师作业附件接口
    @GetMapping("/downloadattachment/{id}")
    public ResponseEntity<InputStreamResource> downloadAssignmentAttachment(@PathVariable("id") String assignmentId) {
        try {
            System.out.println(assignmentId);
            // 调用 Service 获取作业附件文件
            File file = courseAssignmentService.downloadAssignmentAttachment(assignmentId);

            // 对文件名进行 URL 编码，确保中文字符可以正确处理
            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            // 设置响应头，使用 UTF-8 编码格式
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            // 创建文件输入流
            InputStream inputStream = new FileInputStream(file);

            // 返回文件内容，使用 InputStreamResource 实现流式传输
            InputStreamResource resource = new InputStreamResource(inputStream);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException | SQLException e) {
            // 文件未找到或读取错误的处理
            return ResponseEntity.status(404).body(null);
        }
    }

    // 学生上传作业接口
    @PostMapping("/uploadassignment")
    public ResponseEntity<String> uploadAssignment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("course") String courseName,
            @RequestParam("id") String assignmentId,
            @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = extractJwtToken(token);
            String studentId = JWTUtil.extractUserID(jwtToken);

            courseAssignmentService.uploadAssignment(studentId, assignmentId, file);
            return ResponseEntity.ok("作业上传成功");

        } catch (Exception e) {
            return ResponseEntity.status(400).body("上传失败: " + e.getMessage());
        }
    }

    // 教师下载学生作业接口
    @GetMapping("/downloadassignment/{assignmentId}/{studentId}")
    public ResponseEntity<InputStreamResource> downloadStudentAssignment(@PathVariable("assignmentId") String assignmentId,
                                                                         @PathVariable("studentId") String studentId) {
        try {
            System.out.println(assignmentId);
            // 调用 Service 获取学生作业文件
            File file = courseAssignmentService.downloadStudentAssignment(assignmentId, studentId);

            // 对文件名进行 URL 编码，确保中文字符可以正确处理
            String encodedFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            // 设置响应头，使用 UTF-8 编码格式
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            // 创建文件输入流
            InputStream inputStream = new FileInputStream(file);

            // 返回文件内容，使用 InputStreamResource 实现流式传输
            InputStreamResource resource = new InputStreamResource(inputStream);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException | SQLException e) {
            // 文件未找到或读取错误的处理
            return ResponseEntity.status(404).body(null);
        }
    }

    //教师评分
    @PostMapping("/submitscore")
    public ResponseEntity<String> submitScore(@RequestBody ScoreSubmissionDTO scoreSubmissionDTO) {
        try {
            courseAssignmentService.submitScore(scoreSubmissionDTO);
            return ResponseEntity.ok("成功");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("失败：" + e.getMessage());
        }
    }

    // 获取作业信息列表
    @GetMapping("/assignments")
    public ResponseEntity<List<CourseAssignmentDTO>> getAssignments(@RequestParam String course,
                                                                    @RequestHeader("Authorization") String token) {
        try {
            List<CourseAssignmentDTO> assignments = courseAssignmentService.getAssignments(course, token);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
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
