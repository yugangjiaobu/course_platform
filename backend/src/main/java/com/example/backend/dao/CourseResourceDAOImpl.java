package com.example.backend.dao;

import com.example.backend.entity.CourseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CourseResourceDAOImpl implements CourseResourceDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addCourseResource(CourseResource courseResource) {
        String sql = "INSERT INTO course_resources (resource_id, course_id, resource_name, resource_type, resource_path, uploaded_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                courseResource.getResourceId(),
                courseResource.getCourseId(),
                courseResource.getResourceName(),
                courseResource.getResourceType(),
                courseResource.getResourcePath(),
                courseResource.getUploadedAt());
    }

    @Override
    public CourseResource getCourseResourceById(String resourceId) {
        String sql = "SELECT * FROM course_resources WHERE resource_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{resourceId}, new CourseResourceRowMapper());
    }

    @Override
    public List<CourseResource> getAllCourseResources() {
        String sql = "SELECT * FROM course_resources";
        return jdbcTemplate.query(sql, new CourseResourceRowMapper());
    }

    @Override
    public void updateCourseResource(CourseResource courseResource) {
        String sql = "UPDATE course_resources SET course_id = ?, resource_name = ?, resource_type = ?, resource_path = ?, uploaded_at = ? WHERE resource_id = ?";
        jdbcTemplate.update(sql,
                courseResource.getCourseId(),
                courseResource.getResourceName(),
                courseResource.getResourceType(),
                courseResource.getResourcePath(),
                courseResource.getUploadedAt(),
                courseResource.getResourceId());
    }

    @Override
    public void deleteCourseResource(String resourceId) {
        String sql = "DELETE FROM course_resources WHERE resource_id = ?";
        jdbcTemplate.update(sql, resourceId);
    }

    @Override
    public List<CourseResource> getCourseResourcesByCourseId(String courseId) {
        String sql = "SELECT * FROM course_resources WHERE course_id = ?";
        return jdbcTemplate.query(sql, new Object[]{courseId}, new CourseResourceRowMapper());
    }

    private static class CourseResourceRowMapper implements RowMapper<CourseResource> {
        @Override
        public CourseResource mapRow(ResultSet rs, int rowNum) throws SQLException {
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
}
