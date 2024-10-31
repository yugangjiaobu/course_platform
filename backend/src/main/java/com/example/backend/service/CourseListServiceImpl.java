package com.example.backend.service;

import com.example.backend.dao.CourseDAO;
import com.example.backend.dao.UserCourseDAO;
import com.example.backend.dto.CourseListDTO;
import com.example.backend.entity.Course;
import com.example.backend.entity.UserCourse;
import com.example.backend.service.CourseListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseListServiceImpl implements CourseListService{
    @Autowired
    private UserCourseDAO userCourseDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Override
    public List<CourseListDTO> getUserCourses(String username) {
        List<CourseListDTO> courseList = new ArrayList<>();

        try {
            // 1. 通过 userCourseDAO 获取该用户参与的课程
            List<UserCourse> userCourses = userCourseDAO.getAllUserCourses(username);

            // 2. 根据每个 UserCourse 中的 courseId，通过 courseDAO 获取完整的 Course 信息
            for (UserCourse userCourse : userCourses) {
                Course course = courseDAO.getCourseById(userCourse.getCourseId());
                if (course != null) {
                    // 3. 只需要返回课程名，不需要其他信息，组装成 CourseListDTO
                    CourseListDTO courseListDTO = new CourseListDTO(course.getCourseName());
                    courseList.add(courseListDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 你可以在此添加更好的异常处理逻辑
        }

        return courseList;
    }
}