package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.*;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class CourseExamController {
    @Autowired
    private CourseExamService courseExamService;

    // 教师设置考试接口
    @PostMapping("/setexam")
    public String setExam(@RequestParam("coursename") String courseName,
                          @RequestParam("examname") String title,
                          @RequestParam("time") String timeStr, // 接收原始字符串
                          @RequestParam("notice") String notice,
                          @RequestParam("location") String location,
                          @RequestParam("setstate") int setstate,
                          @RequestParam("file") MultipartFile file,
                          @RequestHeader("Authorization") String token) {
        try {
            // 打印接收到的参数
            /*System.out.println(courseName);
            System.out.println(title);
            System.out.println(timeStr);
            System.out.println(notice);
            System.out.println(location);
            System.out.println(setstate);
            System.out.println(file);*/

            // 使用 SimpleDateFormat 手动解析字符串
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // ISO 8601 格式
            Date parsedDate = sdf.parse(timeStr);
            Timestamp time = new Timestamp(parsedDate.getTime()); // 转换为 Timestamp

            // 设置考试数据
            CourseExamDTO courseExamDTO = new CourseExamDTO();
            courseExamDTO.setCourseName(courseName);
            courseExamDTO.setTitle(title);
            courseExamDTO.setExamDate(time);
            courseExamDTO.setDescription(notice);
            courseExamDTO.setRoomName(location);
            courseExamDTO.setSetState(setstate);
            courseExamDTO.setFile(file);

            // 调用业务层设置考试
            courseExamService.setExam(courseExamDTO);
            return "Exam set successfully!";
        } catch (Exception e) {
            return "Error setting exam: " + e.getMessage();
        }
    }


    // 获取考试信息
    @GetMapping("/getexams")
    public List<CourseExamResponseDTO> getExams(@RequestHeader("Authorization") String token,
                                                @RequestParam("coursename") String courseName) throws Exception {
        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);

        return courseExamService.getExams(userId, courseName);
    }

    @GetMapping("/downloadexam/{id}")
    public ResponseEntity<InputStreamResource> downloadExam(@PathVariable("id") String examId) throws IOException {
        try {
            // 调用service层获取文件
            File examFile = courseExamService.downloadExam(examId);

            // 对文件名进行 URL 编码，确保中文字符可以正确处理
            String encodedFilename = URLEncoder.encode(examFile.getName(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

            // 设置响应头，使用 UTF-8 编码格式
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

            // 创建文件输入流
            InputStream inputStream = new FileInputStream(examFile);

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

    // 提取 JWT token
    private String extractJwtToken(String authHeader) {
        String[] parts = authHeader.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            return null;
        }
        return parts[1];
    }
}
