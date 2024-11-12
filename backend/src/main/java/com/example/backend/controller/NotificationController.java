package com.example.backend.controller;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.service.NotificationService;
import com.example.backend.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 发送通知（仅限老师）
    @PostMapping("/send")
    public String sendNotification(@RequestHeader("Authorization") String authHeader, @RequestBody NotificationDTO notificationDTO) {
        // 提取 JWT token
        String token = extractJwtToken(authHeader);

        String role = JWTUtil.extractRole(token);

        System.out.println(notificationDTO.getContent());

        // 如果是学生，直接返回失败
        if (!"teacher".equals(role)) {
            return "学生无权发送通知";
        }

        // 如果是老师，发送通知
        notificationService.sendNotification(token, notificationDTO.getContent()); // 只传入实际内容
        return "通知发送成功";
    }


    // 查看所有通知
    @GetMapping("/view")
    public List<NotificationDTO> viewNotifications(@RequestHeader("Authorization") String authHeader) {
        String token = extractJwtToken(authHeader); // 提取 JWT token
        if (token == null || !JWTUtil.validateToken(token)) {
            throw new RuntimeException("无效的令牌");
        }
        return notificationService.getAllNotifications();
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
