package com.example.backend.dao;

import com.example.backend.entity.Discussion;
import com.example.backend.util.DatabaseUtil;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiscussionDAOImpl implements DiscussionDAO {
    private static final String INSERT_SQL = "INSERT INTO discussions " + "(post_id, course_id, user_id, content, created_at, image_id, image_path, title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM discussions WHERE post_id = ?";
    private static final String UPDATE_SQL = "UPDATE discussions SET course_id = ?, user_id = ?, content = ?, created_at = ? WHERE post_id = ?";
    private static final String DELETE_SQL = "DELETE FROM discussions WHERE post_id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM discussions";
    private static final String FIND_ALL_BY_COURSE_ID_SQL = "SELECT * FROM discussions WHERE course_id = ?";
    private static final String FIND_IMAGES_BY_POST_ID_SQL = "SELECT image_id FROM discussions WHERE post_id = ?";
    private static final String FIND_BY_IMAGE_ID_SQL = "SELECT * FROM discussions WHERE image_id = ?";

    private static final String FIND_BY_USER_ID_SQL = "SELECT * FROM discussions WHERE user_id = ?";

    @Override
    public void save(Discussion discussion) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(1, discussion.getPostId());
            stmt.setString(2, discussion.getCourseId());
            stmt.setString(3, discussion.getUserId());
            stmt.setString(4, discussion.getContent());
            stmt.setTimestamp(5, discussion.getCreatedAt());
            stmt.setString(6, discussion.getImageId());
            stmt.setString(7, discussion.getImagePath());
            stmt.setString(8, discussion.getTitle());
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
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("user_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(Timestamp.valueOf(rs.getString("created_at")));
                discussion.setImageId(rs.getString("image_id"));
                discussion.setImagePath(rs.getString("image_path"));
                //System.out.println(discussion.getImagePath());
                discussion.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
        return discussion;
    }

    @Override
    public void update(Discussion discussion) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, discussion.getCourseId());
            stmt.setString(2, discussion.getUserId());
            stmt.setString(3, discussion.getContent());
            stmt.setString(4, discussion.getPostId());
            stmt.setString(5, discussion.getCreatedAt().toString());
            stmt.setString(6, discussion.getImageId());
            stmt.setString(7, discussion.getImagePath());
            stmt.setString(8, discussion.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
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
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Discussion discussion = new Discussion();
                discussion.setPostId(rs.getString("post_id"));
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("user_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(rs.getTimestamp("created_at"));
                discussion.setImageId(rs.getString("image_id"));
                discussion.setImagePath(rs.getString("image_path"));
                discussion.setTitle(rs.getString("title"));
                discussions.add(discussion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }

    @Override
    public List<Discussion> findAllByCourseId(String courseId) {
        List<Discussion> discussions = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_BY_COURSE_ID_SQL)) {
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Discussion discussion = new Discussion();
                discussion.setPostId(rs.getString("post_id"));
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("user_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(rs.getTimestamp("created_at"));
                discussion.setImageId(rs.getString("image_id"));
                discussion.setImagePath(rs.getString("image_path"));
                discussion.setTitle(rs.getString("title"));
                discussions.add(discussion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }

    @Override
    public List<String> findImagesByPostId(String postId) {
        List<String> imageIds = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_IMAGES_BY_POST_ID_SQL)) {
            stmt.setString(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imageIds.add(rs.getString("image_id")); // 这里改为获取 image_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imageIds;
    }

    @Override
    public Discussion findByImageId(String imageId) {
        Discussion discussion = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_IMAGE_ID_SQL)) {
            stmt.setString(1, imageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                discussion = new Discussion();
                discussion.setPostId(rs.getString("post_id"));
                discussion.setCourseId(rs.getString("course_id"));
                discussion.setUserId(rs.getString("user_id"));
                discussion.setContent(rs.getString("content"));
                discussion.setCreatedAt(rs.getTimestamp("created_at"));
                discussion.setImageId(rs.getString("image_id"));
                discussion.setImagePath(rs.getString("image_path"));
                discussion.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussion;
    }

    @Override
    public List<Discussion> findPostsByUserId(String userId) {
        List<Discussion> discussions = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER_ID_SQL)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                discussions.add(mapResultSetToDiscussion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }

    private Discussion mapResultSetToDiscussion(ResultSet rs) throws SQLException {
        Discussion discussion = new Discussion();
        discussion.setPostId(rs.getString("post_id"));
        discussion.setCourseId(rs.getString("course_id"));
        discussion.setUserId(rs.getString("user_id"));
        discussion.setContent(rs.getString("content"));
        discussion.setCreatedAt(rs.getTimestamp("created_at"));
        discussion.setImageId(rs.getString("image_id"));
        discussion.setImagePath(rs.getString("image_path"));
        discussion.setTitle(rs.getString("title"));
        return discussion;
    }

}
