package com.example.backend.controller;

import com.example.backend.dto.UserInfoDTO;
import com.example.backend.service.UserService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8082") // 替换为你的前端地址
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {

        try {
            String jwtToken = extractJwtToken(token);
            // 从 token 中解析出用户 ID
            String userID = JWTUtil.extractUserID(jwtToken);
            System.out.println(userID);
            // 根据用户 ID 获取用户信息
            UserInfoDTO userInfo = userService.getUserInfo(userID);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("获取用户信息失败");
        }
    }

    @PostMapping("/infoupdate")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token,
                                            @RequestBody UserInfoDTO userInfo) {
        try {
            System.out.println(token);
            String jwtToken = extractJwtToken(token);
            // 从 token 中解析出用户 ID
            String userID = JWTUtil.extractUserID(jwtToken);
            if (userService.updateUserInfo(userID, userInfo)) {
                return ResponseEntity.ok("用户信息更新成功");
            } else {
                return ResponseEntity.status(500).body("用户信息更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("更新用户信息时出错");
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
