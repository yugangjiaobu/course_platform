package com.example.backend.dao;

import com.example.backend.entity.CourseAssignment;

import java.util.List;

public interface CourseAssignmentDAO {
    void addAssignment(CourseAssignment assignment);
    List<CourseAssignment> getAssignmentsByCourseId(String courseId);
    CourseAssignment getAssignmentById(String assignmentId);
}
