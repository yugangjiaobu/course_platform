package com.example.backend.service;

import com.example.backend.dto.CourseResourceDTO;
import com.example.backend.entity.CourseResource;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

public interface CourseResourceService {
    void uploadCourseResource(String courseName, String resourceName, MultipartFile file) throws Exception;
    List<CourseResourceDTO> getCourseResources(String courseName) throws SQLException;
    boolean deleteCourseResource(String resourceId) throws SQLException;
    File downloadCourseResource(String resourceId) throws FileNotFoundException, FileNotFoundException, SQLException;
}
