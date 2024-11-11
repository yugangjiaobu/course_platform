package com.example.backend.controller;

import com.example.backend.dto.CourseDetailsDTO;
import com.example.backend.service.CourseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080") // 替换为你的前端地址
public class CourseDetailsController {

    @Autowired
    private CourseDetailsService courseDetailsService;

    @GetMapping("/coursedetails")
    public ResponseEntity<?> getCourseDetails(@RequestParam String name, @RequestHeader("Authorization") String token) {
        try {
            System.out.println(name);
            CourseDetailsDTO courseDTO = courseDetailsService.getCourseDetailsByName(name, token);
            return ResponseEntity.ok(courseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}