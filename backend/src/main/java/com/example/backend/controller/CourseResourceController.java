package com.example.backend.controller;

import com.example.backend.dto.CourseResourceDTO;
import com.example.backend.entity.CourseResource;
import com.example.backend.service.CourseResourceService;
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
public class CourseResourceController {

    @Autowired
    private CourseResourceService courseResourceService;

    // 获取课程资源列表接口
    @GetMapping("/getresource")
    public ResponseEntity<List<CourseResourceDTO>> getCourseResources(
            @RequestParam("coursename") String courseName,
            @RequestHeader("Authorization") String token) throws SQLException {
        List<CourseResourceDTO> resources = courseResourceService.getCourseResources(courseName);
        return ResponseEntity.ok(resources);
    }

    // 上传资源接口
    @PostMapping("/uploadresource")
    public ResponseEntity<String> uploadCourseResource(
            @RequestParam("coursename") String courseName,
            @RequestParam("file") MultipartFile file,
            @RequestParam("resourcename") String resourceName,
            @RequestHeader("Authorization") String token) {
        try {
            courseResourceService.uploadCourseResource(courseName, resourceName, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("资源上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
        }
    }

    //删除资源接口
    @PostMapping("/deleteresource")
    public ResponseEntity<String> deleteCourseResource(@RequestBody CourseResourceDTO resourceDTO) {
        try {
            System.out.println(resourceDTO.getId());
            boolean isDeleted = courseResourceService.deleteCourseResource(resourceDTO.getId());
            if (isDeleted) {
                return ResponseEntity.ok("Resource deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Resource not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete resource: " + e.getMessage());
        }
    }

    // 下载资源接口
    @GetMapping("/downloadresource/{id}")
    public ResponseEntity<InputStreamResource> downloadResource(@PathVariable("id") String resourceId) throws IOException {
        try {
            System.out.println(resourceId);
            // 获取文件
            File file = courseResourceService.downloadCourseResource(resourceId);

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
}
