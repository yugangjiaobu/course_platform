package com.example.backend.service;

import com.example.backend.dao.CourseDAO;
import com.example.backend.dao.UserCourseDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.dto.CourseDetailsDTO;
import com.example.backend.entity.Course;
import com.example.backend.entity.User;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseDetailsServiceImpl implements CourseDetailsService {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public CourseDetailsDTO getCourseDetailsByName(String name, String jwtToken) throws Exception {

        String token = extractJwtToken(jwtToken);
        // 获取用户角色
        String role = JWTUtil.extractRole(token);
        System.out.println(name);

        // 获取课程信息
        Course course = courseDAO.getCourseByName(name);
        if (course == null) {
            throw new Exception("Course not found");
        }
        System.out.println(course.getCourseName());

        // 获取教师信息
        User teacher = userDAO.getUserById(course.getTeacherId());
        if (teacher == null) {
            throw new Exception("Teacher not found");
        }
        System.out.println(teacher.getName());

        // 获取学生信息
        List<User> students = new ArrayList<>();
        List<User> courseStudents = courseDAO.getStudentsByCourseId(course.getCourseId());
        if (courseStudents != null) {
            students.addAll(courseStudents);
        }

        // 生成课程的DTO对象
        CourseDetailsDTO courseDTO = new CourseDetailsDTO();
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setCourseDescription(course.getCourseDescription());
        courseDTO.setTeacherName(teacher.getName());
        courseDTO.setTeacherEmail(teacher.getEmail());
        courseDTO.setStudents(students);
        courseDTO.setSyllabus(List.of("迟到一次不及格", "迟到两次满分"));
        courseDTO.setIsTeacher(role.equals("teacher"));

        return courseDTO;
    }

    // 提取 JWT token
    private String extractJwtToken(String authHeader) {
        String[] parts = authHeader.split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            return null;
        }
        return parts[1];
    }
}
