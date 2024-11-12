package com.example.backend.service;

import com.example.backend.dao.NotificationDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.dto.NotificationDTO;
import com.example.backend.entity.Notification;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void sendNotification(String token, String content) {
        // 从 token 中提取用户信息
        String senderId = JWTUtil.extractUserID(token);
        String senderName;

        // 获取用户的姓名
        try {
            senderName = userDAO.getUserById(senderId).getName();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败: " + e.getMessage(), e);
        }

        // 创建通知对象
        Notification notification = new Notification();
        notification.setNotificationId(generateRandomId());
        notification.setSenderId(senderId);
        notification.setContent(content);
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        notification.setSenderName(senderName);

        // 将通知存储到数据库
        notificationDAO.addNotification(notification);
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationDAO.findAllNotifications();
        return notifications.stream().map(notification -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setContent(notification.getContent());
            dto.setSentAt(notification.getSentAt());
            dto.setSenderName(notification.getSenderName());
            return dto;
        }).collect(Collectors.toList());
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }
}
