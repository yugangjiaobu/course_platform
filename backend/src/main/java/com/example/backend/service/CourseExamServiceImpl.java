package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.dto.*;
import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseExamServiceImpl implements CourseExamService {

    @Autowired
    private CourseExamDAO courseExamDAO;

    @Autowired
    private ExamRoomDAO examRoomDAO;

    @Autowired
    private ExaminerDAO examinerDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserCourseDAO userCourseDAO;

    private static final String EXAM_PAPER_PATH = "E:\\Idea_Project\\exam\\";

    @Override
    public void setExam(CourseExamDTO courseExamDTO) throws Exception {
/*
        System.out.println(courseExamDTO.getCourseName());
        System.out.println(courseExamDTO.getExamDate());
        System.out.println(courseExamDTO.getSetState());
        System.out.println(courseExamDTO.getRoomName());
        System.out.println(courseExamDTO.getDescription());
        System.out.println(courseExamDTO.getTitle());
        System.out.println(courseExamDTO.getFile());*/

        // 1. 获取课程ID
        Course course = courseDAO.getCourseByName(courseExamDTO.getCourseName());
        String courseId = course.getCourseId();

        // 2. 生成exam_id
        String examId = UUID.randomUUID().toString();

        // 3. 保存考试信息到数据库
        CourseExam courseExam = new CourseExam();
        courseExam.setExamId(examId);
        courseExam.setCourseId(courseId);
        courseExam.setTitle(courseExamDTO.getTitle());
        courseExam.setDescription(courseExamDTO.getDescription());
        courseExam.setExamDate(courseExamDTO.getExamDate());

        // 文件保存：根据给定目录生成文件路径
        MultipartFile file = courseExamDTO.getFile();
        String filePath = EXAM_PAPER_PATH + examId + "_" + file.getOriginalFilename(); // 使用examId作为文件前缀
        File examFile = new File(filePath);
        file.transferTo(examFile);

        // 保存文件路径到数据库
        courseExam.setCreatedBy(filePath);  // 存储文件路径
        courseExam.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        courseExam.setUpdatedAt(null);
        courseExam.setAnswer(null);

        courseExamDAO.save(courseExam);

        //System.out.println(courseExamDTO.getRoomName());

        // 4. 保存考场信息
        ExamRoom examRoom = new ExamRoom();
        examRoom.setExamId(examId);
        examRoom.setRoomName(courseExamDTO.getRoomName());
        examRoom.setSeatingOrder(null);
        String roomId = UUID.randomUUID().toString();

        //System.out.println(examRoom.getRoomName());

        examRoom.setRoomId(roomId);
        examRoomDAO.save(examRoom);

        //System.out.println("Course ID: " + courseId);

        // 5. 随机或按学号排序分配座位
        List<UserCourse> students = userCourseDAO.getAllStudentsByCourseId(courseId);
        List<Examiner> examiners = new ArrayList<>();

        // 判断是否随机分配座位
        if (courseExamDTO.getSetState() == 0) {  // 按学号排序
            students.sort((a, b) -> a.getUserId().compareTo(b.getUserId())); // 按学号排序
        } else if (courseExamDTO.getSetState() == 1) {  // 随机排序
            students.sort((a, b) -> Math.random() > 0.5 ? 1 : -1);  // 随机排序
        }

        //System.out.println("Number of students: " + students.size());

        // 分配座位
        int seatNum = 1;
        for (UserCourse student : students) {
            Examiner examiner = new Examiner();
            examiner.setName(userDAO.getUserById(student.getUserId()).getName());
            examiner.setUserId(student.getUserId());
            examiner.setExamNum(UUID.randomUUID().toString()); // 生成唯一的考试号
            examiner.setSeatNum(seatNum++);
            examiner.setExamId(examId);
            examiner.setRoomId(roomId);
            examiner.setStuAnswer(null);
            examiners.add(examiner);
        }

        // 保存考试人员信息
        for (Examiner examiner : examiners) {
            examinerDAO.save(examiner);
        }
    }

    @Override
    public List<CourseExamResponseDTO> getExams(String userId, String courseName) throws Exception {
        // 获取当前用户的角色
        User user = userDAO.getUserById(userId);
        String role = user.getRole();

        // 根据课程名称获取考试列表
        List<CourseExam> exams = courseExamDAO.getExamsByCourseName(courseName);

        // 将考试信息转化为返回DTO
        if ("student".equals(role)) {
            // 如果是学生，返回带有座位号和试卷下载链接的考试信息
            return exams.stream().map(exam -> {
                CourseExamResponseDTO dto = new CourseExamResponseDTO();
                dto.setExamId(exam.getExamId());
                dto.setExamName(exam.getTitle());
                dto.setTime(exam.getExamDate());
                dto.setNotice(exam.getDescription());

                // 获取考试考场信息
                ExamRoom examRoom = null;
                try {
                    examRoom = examRoomDAO.getByExamId(exam.getExamId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (examRoom != null) {
                    dto.setLocation(examRoom.getRoomName());
                }

                // 返回下载链接
                String downloadUrl = "/api/downloadexam/" + exam.getExamId(); // 使用下载链接格式
                dto.setUrl(downloadUrl);

                // 获取学生座位号
                Examiner examiner = null;
                try {
                    examiner = examinerDAO.getExaminerByExamIdAndUserId(exam.getExamId(), userId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                dto.setSeatId(examiner != null ? String.valueOf(examiner.getSeatNum()) : "N/A");
                return dto;
            }).collect(Collectors.toList());
        } else if ("teacher".equals(role)) {
            // 如果是教师，返回不带座位号的考试信息
            return exams.stream().map(exam -> {
                CourseExamResponseDTO dto = new CourseExamResponseDTO();
                dto.setExamId(exam.getExamId());
                dto.setExamName(exam.getTitle());
                dto.setTime(exam.getExamDate());
                dto.setNotice(exam.getDescription());

                // 获取考试考场信息
                ExamRoom examRoom = null;
                try {
                    examRoom = examRoomDAO.getByExamId(exam.getExamId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (examRoom != null) {
                    dto.setLocation(examRoom.getRoomName());
                }

                // 返回下载链接
                String downloadUrl = "/api/downloadexam/" + exam.getExamId(); // 使用下载链接格式
                dto.setUrl(downloadUrl);

                return dto;
            }).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public File downloadExam(String examId) throws FileNotFoundException, SQLException {
        // 获取考试信息
        CourseExam courseExam = courseExamDAO.getCourseExamById(examId);
        if (courseExam == null) {
            throw new FileNotFoundException("考试信息未找到");
        }

        // 获取试卷路径
        String examFilePath = courseExam.getCreatedBy();
        File file = new File(examFilePath);

        if (!file.exists()) {
            throw new FileNotFoundException("试卷文件未找到");
        }

        return file;
    }
}

