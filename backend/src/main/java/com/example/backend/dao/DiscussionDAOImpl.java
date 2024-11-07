package com.example.backend.dao;

import com.example.backend.entity.Discussion;
import com.example.backend.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscussionDAOImpl implements DiscussionDAO {
    private static final String INSERT_SQL = "INSERT INTO discussions (post_id, course_id, user_id, content) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM discussions WHERE post_id = ?";
    // Other SQL queries...
    private static final String UPDATE_SQL = "UPDATE discussions SET course_id = ?, user_id = ?, content = ?, created_at = ? WHERE post_id = ?";
    private static final String DELETE_SQL = "DELETE FROM discussions WHERE post_id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM discussions";

    @Override
    public void save(Discussion discussion) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, discussion.getPostId());
            stmt.setString(2, discussion.getCourseId());
            stmt.setString(3, discussion.getUserId());
            stmt.setString(4, discussion.getContent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }

    @Override
    public Discussion findById(String postId) {
        Discussion discussion = null;
        try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setString(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                discussion = new Discussion();
                discussion.setPostId(rs.getString("post_id"));
                // Set other fields...
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("user_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
        return discussion;
    }

    // Implement other methods...
    public void update(Discussion discussion) {
        try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, discussion.getCourseId());
            stmt.setString(2, discussion.getUserId());
            stmt.setString(3, discussion.getContent());
            stmt.setString(4, discussion.getCreatedAt().toString());
            stmt.setString(5, discussion.getPostId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Discussion discussion) {
        try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, discussion.getPostId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Discussion> findAll() {
        List<Discussion> discussions = new ArrayList<>();
        // Implement findAll...
        Discussion discussion = null;
        try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                discussion = new Discussion();
                discussion.setPostId(rs.getString("post_id"));
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("course_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
                discussions.add(discussion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }
}