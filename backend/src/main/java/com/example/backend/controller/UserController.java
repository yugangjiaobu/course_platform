package com.example.backend.controller;


import com.example.backend.dto.UserInfoDTO;
import com.example.backend.service.UserService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/userinfo")
    public ResponseEntity<UserInfoDTO> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            // 从 token 中解析出用户 ID
            String userId = JWTUtil.extractUserID(token);
            // 根据用户 ID 获取用户信息
            UserInfoDTO userInfo = userService.getUserInfo(userId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // 返回 400 错误
        }
    }
}

