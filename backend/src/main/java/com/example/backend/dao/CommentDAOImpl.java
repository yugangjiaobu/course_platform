package com.example.backend.dao;
import com.example.backend.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.backend.util.DatabaseUtil;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDAOImpl implements CommentDAO {
    private static final String INSERT_SQL = "INSERT INTO comments (comment_id, post_id, user_id, content, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM comments WHERE comment_id = ?";
    private static final String UPDATE_SQL = "UPDATE comments SET post_id = ?, user_id = ?, content = ?, created_at = ? WHERE comment_id = ?";
    private static final String DELETE_SQL = "DELETE FROM comments WHERE comment_id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM comments";
    private static final String FIND_BY_POST_ID_SQL = "SELECT * FROM comments WHERE post_id = ?";

    @Override
    public void save(Comment comment) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, comment.getCommentId());
            stmt.setString(2, comment.getPostId());
            stmt.setString(3, comment.getUserId());
            stmt.setString(4, comment.getContent());
            stmt.setString(5, comment.getCreatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Comment findById(String commentId) {
        Comment comment = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setString(1, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comment = new Comment();
                comment.setCommentId(rs.getString("comment_id"));
                comment.setPostId(rs.getString("post_id"));
                comment.setUserId(rs.getString("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    @Override
    public void update(Comment comment) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, comment.getPostId());
            stmt.setString(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            stmt.setString(4, comment.getCreatedAt().toString());
            stmt.setString(5, comment.getCommentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Comment comment) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, comment.getCommentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { // 使用 while 确保遍历所有记录
                Comment comment = new Comment();
                comment.setCommentId(rs.getString("comment_id"));
                comment.setPostId(rs.getString("post_id"));
                comment.setUserId(rs.getString("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comments.add(comment); // 添加每一条评论到列表中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }


    @Override
    public List<Comment> findByPostId(String postId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_POST_ID_SQL)) {
            stmt.setString(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setCommentId(rs.getString("comment_id"));
                comment.setPostId(rs.getString("post_id"));
                comment.setUserId(rs.getString("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
        return comments;
    }
}
