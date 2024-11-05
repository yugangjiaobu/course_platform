package com.example.backend.dao;

import com.example.backend.entity.Notification;

public interface NotificationDAO {
    void addNotification(Notification notification);
    Notification getNotificationById(String notificationId);
    void updateNotification(Notification notification);
    void deleteNotification(String notificationId);
}