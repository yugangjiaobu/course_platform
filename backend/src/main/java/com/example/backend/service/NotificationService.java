package com.example.backend.service;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entity.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(String token, String content);
    List<NotificationDTO> getAllNotifications();
}
