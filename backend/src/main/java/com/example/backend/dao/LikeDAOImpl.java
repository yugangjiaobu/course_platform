package com.example.backend.dao;// LikeDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.backend.entity.Like;
import com.example.backend.util.DatabaseUtil;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDAOImpl implements LikeDAO {
    private static final String INSERT_SQL = "INSERT INTO likes (like_id, post_id, user_id, created_at) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM likes WHERE like_id = ?";
    private static final String UPDATE_SQL = "UPDATE likes SET post_id = ?, comment_id = ?, user_id = ?, created_at = ? WHERE like_id = ?";
    private static final String DELETE_SQL = "DELETE FROM likes WHERE like_id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM likes";
    private static final String COUNT_LIKES_SQL = "SELECT COUNT(*) FROM likes WHERE post_id = ?";
    private static final String FIND_BY_POST_AND_USER_SQL = "SELECT * FROM likes WHERE post_id = ? AND user_id = ?";

    private static final String FIND_LIKES_BY_USER_SQL = "SELECT * FROM likes WHERE user_id = ?";

    @Override
    public void save(Like like) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, like.getLikeId());
            stmt.setString(2, like.getPostId());
            stmt.setString(3, like.getUserId());
            stmt.setString(4, like.getCreatedAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Like findById(String likeId) {
        Like like = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setString(1, likeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                like = new Like();
                like.setLikeId(rs.getString("like_id"));
                like.setPostId(rs.getString("post_id"));
                like.setUserId(rs.getString("user_id"));
                like.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return like;
    }

    @Override
    public void update(Like like) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, like.getPostId());
            stmt.setString(2, like.getUserId());
            stmt.setString(3, like.getCreatedAt().toString());
            stmt.setString(4, like.getLikeId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Like like) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setString(1, like.getLikeId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Like> findAll() {
        List<Like> likes = new ArrayList<>();
        Like like = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                like = new Like();
                like.setLikeId(rs.getString("like_id"));
                like.setPostId(rs.getString("post_id"));
                like.setUserId(rs.getString("user_id"));
                like.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    @Override
    public Like findByPostIdAndUserId(String postId, String userId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_POST_AND_USER_SQL)) {
            stmt.setString(1, postId);
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Like like = new Like();
                like.setLikeId(rs.getString("like_id"));
                like.setPostId(rs.getString("post_id"));
                like.setUserId(rs.getString("user_id"));
                like.setCreatedAt(rs.getTimestamp("created_at"));
                return like;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int countLikesByPostId(String postId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_LIKES_SQL)) {
            stmt.setString(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Like> findLikesByUserId(String userId) {
        List<Like> likes = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_LIKES_BY_USER_SQL)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Like like = new Like();
                like.setLikeId(rs.getString("like_id"));
                like.setPostId(rs.getString("post_id"));
                like.setUserId(rs.getString("user_id"));
                like.setCreatedAt(rs.getTimestamp("created_at"));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

}
