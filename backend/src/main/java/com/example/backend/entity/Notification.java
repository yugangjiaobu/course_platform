package com.example.backend.entity;

import java.sql.Timestamp;

public class Notification {
    private String notificationId;
    private String senderId;
    private String receiverId;
    private String courseId;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
    private String senderName;
  
    // Getters and setters  
    public String getNotificationId() {  
        return notificationId;  
    }

    public void setNotificationId(String notificationId) {  
        this.notificationId = notificationId;  
    }

    public String getSenderId() {  
        return senderId;  
    }

    public void setSenderId(String senderId) {  
        this.senderId = senderId;  
    }

    public String getReceiverId() {  
        return receiverId;  
    }

    public void setReceiverId(String receiverId) {  
        this.receiverId = receiverId;  
    }

    public String getCourseId() {  
        return courseId;  
    }

    public void setCourseId(String courseId) {  
        this.courseId = courseId;  
    }

    public String getContent() {  
        return content;  
    }

    public void setContent(String content) {  
        this.content = content;  
    }

    public Timestamp getSentAt() {  
        return sentAt;  
    }

    public void setSentAt(Timestamp sentAt) {  
        this.sentAt = sentAt;  
    }

    public boolean isRead() {  
        return isRead;  
    }

    public void setRead(boolean read) {  
        isRead = read;  
    }

    @Override  
    public String toString() {
        return "Notification{" +  
                "notificationId='" + notificationId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", isRead=" + isRead +
                '}';
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}