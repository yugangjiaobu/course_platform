package com.example.backend.dao;

import com.example.backend.entity.CourseResource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseResourceDAOImpl implements CourseResourceDAO {
    private Connection connection;

    public CourseResourceDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCourseResource(CourseResource courseResource) throws SQLException {
        String sql = "INSERT INTO course_resources (resource_id, course_id, resource_name, resource_type, resource_path, uploaded_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseResource.getResourceId());
            stmt.setString(2, courseResource.getCourseId());
            stmt.setString(3, courseResource.getResourceName());
            stmt.setString(4, courseResource.getResourceType());
            stmt.setString(5, courseResource.getResourcePath());
            stmt.setTimestamp(6, courseResource.getUploadedAt());
            stmt.executeUpdate();
        }
    }

    @Override
    public CourseResource getCourseResourceById(String resourceId) throws SQLException {
        String sql = "SELECT * FROM course_resources WHERE resource_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, resourceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CourseResource courseResource = new CourseResource();
                courseResource.setResourceId(rs.getString("resource_id"));
                courseResource.setCourseId(rs.getString("course_id"));
                courseResource.setResourceName(rs.getString("resource_name"));
                courseResource.setResourceType(rs.getString("resource_type"));
                courseResource.setResourcePath(rs.getString("resource_path"));
                courseResource.setUploadedAt(rs.getTimestamp("uploaded_at"));
                return courseResource;
            }
        }
        return null;
    }

    @Override
    public List<CourseResource> getAllCourseResources() throws SQLException {
        List<CourseResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM course_resources";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CourseResource courseResource = new CourseResource();
                courseResource.setResourceId(rs.getString("resource_id"));
                courseResource.setCourseId(rs.getString("course_id"));
                courseResource.setResourceName(rs.getString("resource_name"));
                courseResource.setResourceType(rs.getString("resource_type"));
                courseResource.setResourcePath(rs.getString("resource_path"));
                courseResource.setUploadedAt(rs.getTimestamp("uploaded_at"));
                resources.add(courseResource);
            }
        }
        return resources;
    }

    @Override
    public void updateCourseResource(CourseResource courseResource) throws SQLException {
        String sql = "UPDATE course_resources SET course_id = ?, resource_name = ?, resource_type = ?, resource_path = ?, uploaded_at = ? WHERE resource_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseResource.getCourseId());
            stmt.setString(2, courseResource.getResourceName());
            stmt.setString(3, courseResource.getResourceType());
            stmt.setString(4, courseResource.getResourcePath());
            stmt.setTimestamp(5, courseResource.getUploadedAt());
            stmt.setString(6, courseResource.getResourceId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteCourseResource(String resourceId) throws SQLException {
        String sql = "DELETE FROM course_resources WHERE resource_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, resourceId);
            stmt.executeUpdate();
        }
    }
}