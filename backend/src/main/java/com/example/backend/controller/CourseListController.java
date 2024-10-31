package com.example.backend.controller;

import com.example.backend.dto.CourseListDTO;
import com.example.backend.service.CourseListService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/api/courselist")
@CrossOrigin(origins = "http://localhost:8081") // 替换为你的前端地址
public class CourseListController {

    @Autowired
    private CourseListService courselistService;

    @GetMapping
    public ResponseEntity<List<CourseListDTO>> getUserCourses(@RequestHeader("Authorization") String token) {
        // 从请求头中提取 JWT token
        String jwtToken = extractJwtToken(token);

        // 验证 token
        if (!JWTUtil.validateToken(jwtToken)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        // 从 token 中提取 username
        String username = JWTUtil.extractUsername(jwtToken);

        // 检查 token 是否过期
        if (JWTUtil.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        // 调用 service 获取用户课程列表
        List<CourseListDTO> courseNames = courselistService.getUserCourses(username);
        return ResponseEntity.ok(courseNames);
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