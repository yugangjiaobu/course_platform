package com.example.backend.dao;

import com.example.backend.entity.CourseResource;

import java.sql.SQLException;
import java.util.List;

public interface CourseResourceDAO {
    void addCourseResource(CourseResource courseResource) throws SQLException;
    CourseResource getCourseResourceById(String resourceId) throws SQLException;
    List<CourseResource> getAllCourseResources() throws SQLException;
    void updateCourseResource(CourseResource courseResource) throws SQLException;
    void deleteCourseResource(String resourceId) throws SQLException;
}