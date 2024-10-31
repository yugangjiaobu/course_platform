package com.example.backend.service;

import com.example.backend.dto.CourseListDTO;

import java.util.List;

public interface CourseListService {
    List<CourseListDTO> getUserCourses(String username);
}
