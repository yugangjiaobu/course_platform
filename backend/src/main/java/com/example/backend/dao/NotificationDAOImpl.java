package com.example.backend.dao;

import com.example.backend.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class NotificationDAOImpl implements NotificationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create
    @Override
    public void addNotification(Notification notification) {
        String sql = "INSERT INTO notifications (notification_id, sender_id, content, sent_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, notification.getNotificationId(), notification.getSenderId(),
                notification.getContent(), notification.getSentAt());
    }

    @Override
    public List<Notification> findAllNotifications() {
        String sql = "SELECT n.notification_id, n.sender_id, n.content, n.sent_at, u.name AS sender_name " +
                "FROM notifications n JOIN users u ON n.sender_id = u.user_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Notification notification = new Notification();
            notification.setNotificationId(rs.getString("notification_id"));
            notification.setSenderId(rs.getString("sender_id"));
            notification.setContent(rs.getString("content"));
            notification.setSentAt(rs.getTimestamp("sent_at"));
            notification.setSenderName(rs.getString("sender_name")); // 设定发送者名字
            return notification;
        });
    }

    // Read
    public Notification getNotificationById(String notificationId) {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{notificationId}, (rs, rowNum) -> {
            Notification notification = new Notification();
            notification.setNotificationId(rs.getString("notification_id"));
            notification.setSenderId(rs.getString("sender_id"));
            notification.setReceiverId(rs.getString("receiver_id"));
            notification.setCourseId(rs.getString("course_id"));
            notification.setContent(rs.getString("content"));
            notification.setSentAt(rs.getTimestamp("sent_at"));
            notification.setRead(rs.getInt("is_read") == 1);
            return notification;
        });
    }

    // Update
    public void updateNotification(Notification notification) {
        String sql = "UPDATE notifications SET sender_id = ?, receiver_id = ?, course_id = ?, content = ?, sent_at = ?, is_read = ? " +
                "WHERE notification_id = ?";
        jdbcTemplate.update(sql, notification.getSenderId(), notification.getReceiverId(),
                notification.getCourseId(), notification.getContent(), notification.getSentAt(),
                notification.isRead() ? 1 : 0, notification.getNotificationId());
    }

    // Delete
    public void deleteNotification(String notificationId) {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        jdbcTemplate.update(sql, notificationId);
    }
}
