package com.example.backend.controller;

import com.example.backend.service.LikeService;
import com.example.backend.dto.*;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/getlikelist")
    public List<LikedPostDTO> getLikedPosts(@RequestHeader("Authorization") String token) throws Exception {
        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);
        return likeService.getLikedPosts(userId);
    }

    @GetMapping("/getmylist")
    public List<UserPostDTO> getMyPosts(@RequestHeader("Authorization") String token) throws SQLException {
        String jwtToken = extractJwtToken(token);
        String userId = JWTUtil.extractUserID(jwtToken);
        return likeService.getUserPosts(userId);
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
