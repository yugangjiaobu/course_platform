package com.example.backend.service;

import com.example.backend.dao.CourseDAO;
import com.example.backend.dao.CourseResourceDAO;
import com.example.backend.dto.CourseResourceDTO;
import com.example.backend.entity.Course;
import com.example.backend.entity.CourseResource;
import com.example.backend.service.CourseResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseResourceServiceImpl implements CourseResourceService {

    @Autowired
    private CourseResourceDAO courseResourceDAO;

    @Autowired
    private CourseDAO courseDAO;

    private final String resourceDirectory = "E:\\Idea_Project\\resourse"; // 本地资源文件夹路径

    @Override
    public void uploadCourseResource(String courseName, String resourceName, MultipartFile file) throws Exception {
        String resourceId = UUID.randomUUID().toString();
        String resourcePath = resourceDirectory + File.separator + file.getOriginalFilename();

        // 通过课程名称获取课程ID
        Course course = courseDAO.getCourseByName(courseName);
        if (course == null) {
            throw new Exception("课程不存在");
        }
        String courseId = course.getCourseId();

        CourseResource courseResource = new CourseResource();
        courseResource.setResourceId(resourceId);
        courseResource.setCourseId(courseId);
        courseResource.setResourceName(resourceName);
        courseResource.setResourcePath(resourcePath);
        courseResource.setUploadedAt(new Timestamp(System.currentTimeMillis()));

        file.transferTo(new File(resourcePath));
        courseResourceDAO.addCourseResource(courseResource);
    }

    @Override
    public List<CourseResourceDTO> getCourseResources(String courseName) throws SQLException {
        Course course = courseDAO.getCourseByName(courseName);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        String courseId = course.getCourseId();

        return courseResourceDAO.getCourseResourcesByCourseId(courseId).stream()
                .map(resource -> new CourseResourceDTO(
                        resource.getResourceName(),
                        resource.getResourceId(),
                        "/api/downloadresource/" + resource.getResourceId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCourseResource(String resourceId) throws SQLException {
        // 从数据库获取资源信息
        CourseResource courseResource = courseResourceDAO.getCourseResourceById(resourceId);

        if (courseResource != null) {
            // 删除本地文件
            String filePath = courseResource.getResourcePath();
            File file = new File(filePath);

            if (file.exists()) {
                boolean fileDeleted = file.delete();
                if (!fileDeleted) {
                    throw new RuntimeException("Failed to delete the file.");
                }
            }

            // 删除数据库中的记录
            courseResourceDAO.deleteCourseResource(resourceId);
            return true;
        }

        return false; // 资源不存在
    }


    @Override
    public File downloadCourseResource(String resourceId) throws FileNotFoundException, SQLException {
        // 获取资源路径
        CourseResource resource = courseResourceDAO.getCourseResourceById(resourceId);
        if (resource == null) {
            throw new FileNotFoundException("资源未找到");
        }

        // 获取文件的绝对路径
        String resourcePath = resource.getResourcePath();

        // 创建 File 对象
        File file = new File(resourcePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }

        return file;
    }
}
