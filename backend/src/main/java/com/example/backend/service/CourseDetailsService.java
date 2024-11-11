package com.example.backend.service;

import com.example.backend.dto.CourseDetailsDTO;

public interface CourseDetailsService {
    CourseDetailsDTO getCourseDetailsByName(String name, String token) throws Exception;
}