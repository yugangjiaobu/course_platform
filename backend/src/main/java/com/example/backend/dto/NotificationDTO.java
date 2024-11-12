package com.example.backend.dto;

import java.sql.Timestamp;

public class NotificationDTO {
    private String content;
    private Timestamp sentAt;
    private String senderName; // 老师名字

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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
