package com.example.backend.dao;

import com.example.backend.entity.CourseAssignment;

import java.sql.SQLException;
import java.util.List;

public interface CourseAssignmentDAO {
    void addCourseAssignment(CourseAssignment assignment) throws SQLException;
    CourseAssignment getCourseAssignment(int assignmentId) throws SQLException;
    List<CourseAssignment> getAllCourseAssignments() throws SQLException;
    void updateCourseAssignment(CourseAssignment assignment) throws SQLException;
    void deleteCourseAssignment(int assignmentId) throws SQLException;
}