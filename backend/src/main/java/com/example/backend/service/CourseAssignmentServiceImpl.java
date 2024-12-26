package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseAssignmentServiceImpl implements CourseAssignmentService {
    @Autowired
    private CourseAssignmentDAO courseAssignmentDAO;

    @Autowired
    private StudentAssignmentDAO studentAssignmentDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private UserDAO userDAO;

    private final String assignmentDirectory_t = "E:\\Idea_Project\\T-Assignment";
    private final String assignmentDirectory_u = "E:\\Idea_Project\\U-Assignment"; // 本地存储路径// 本地存储路径

    @Override
    public void createAssignment(String courseName, String title, String description, String dueDate, MultipartFile file) throws Exception {

        String assignmentId = UUID.randomUUID().toString();
        String filePath = assignmentDirectory_t + File.separator + file.getOriginalFilename();

        Course course = courseDAO.getCourseByName(courseName);
        if (course == null) {
            throw new Exception("课程不存在");
        }
        String courseId = course.getCourseId();

        CourseAssignment assignment = new CourseAssignment();
        assignment.setAssignmentId(assignmentId);
        assignment.setCourseId(courseId);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(Timestamp.valueOf(dueDate + " 23:59:59"));
        assignment.setCreatedBy(filePath);
        assignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        assignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        file.transferTo(new File(filePath));

        courseAssignmentDAO.addAssignment(assignment);
    }

    @Override
    public File downloadAssignmentAttachment(String assignmentId) throws FileNotFoundException, SQLException {
        // 获取作业信息
        CourseAssignment assignment = courseAssignmentDAO.getAssignmentById(assignmentId);
        if (assignment == null) {
            throw new FileNotFoundException("作业未找到");
        }

        // 获取文件路径
        String filePath = assignment.getCreatedBy();
        if (filePath == null || filePath.isEmpty()) {
            throw new FileNotFoundException("作业附件不存在");
        }

        // 创建文件对象并验证文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("附件文件未找到");
        }

        return file;
    }

    @Override
    public void uploadAssignment(String studentId, String assignmentId, MultipartFile file) throws Exception {
        // 获取作业信息
        if (courseAssignmentDAO.getAssignmentById(assignmentId) == null) {
            throw new Exception("作业不存在");
        }

        // 生成文件路径并保存文件
        String filePath = assignmentDirectory_u + File.separator + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        //System.out.println(filePath);
        //System.out.println(assignmentId);

        // 创建学生作业记录
        StudentAssignment submission = new StudentAssignment();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setSubmissionContent(filePath);  // 保存文件路径
        submission.setSubmissionTime(new Timestamp(System.currentTimeMillis()));

        // 保存作业提交
        studentAssignmentDAO.saveSubmission(submission);
    }

    @Override
    public File downloadStudentAssignment(String assignmentId, String studentId) throws FileNotFoundException, SQLException {
        // 获取学生作业信息
        StudentAssignment studentAssignment = studentAssignmentDAO.getStudentAssignmentById(assignmentId, studentId);
        if (studentAssignment == null) {
            throw new FileNotFoundException("作业未找到");
        }

        // 获取作业文件路径
        String filePath = studentAssignment.getSubmissionContent();
        if (filePath == null || filePath.isEmpty()) {
            throw new FileNotFoundException("作业附件不存在");
        }

        // 创建文件对象并验证文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("附件文件未找到");
        }

        return file;
    }

    @Override
    public void submitScore(ScoreSubmissionDTO scoreSubmissionDTO) throws Exception {
        // 获取作业提交记录
        StudentAssignment studentAssignment = studentAssignmentDAO.getSubmissionByStudentAndAssignment(
                scoreSubmissionDTO.getSid(), scoreSubmissionDTO.getAid());

        if (studentAssignment == null) {
            throw new Exception("作业提交记录不存在");
        }

        // 更新分数
        double score = Double.parseDouble(scoreSubmissionDTO.getScore());
        studentAssignment.setGrade(score);
        studentAssignmentDAO.updateScore(studentAssignment);
    }

    @Override
    public List<CourseAssignmentDTO> getAssignments(String courseName, String token) throws Exception {
        // 获取作业列表
        Course course = courseDAO.getCourseByName(courseName);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        String courseId = course.getCourseId();
        List<CourseAssignment> assignments = courseAssignmentDAO.getAssignmentsByCourseId(courseId);

        // 解析JWT token获取用户角色
        String jwtToken = extractJwtToken(token);
        String role = JWTUtil.extractRole(jwtToken);
        String userId = JWTUtil.extractUserID(jwtToken);
        boolean isTeacher = role.equals("teacher"); // 判断请求方是否是教师

        return assignments.stream()
                .map(assignment -> {

                    StudentAssignment studentAssignment = studentAssignmentDAO.getStudentAssignmentById(assignment.getAssignmentId(), userId);

                    // 构建 CourseAssignmentDTO
                    CourseAssignmentDTO assignmentDTO = new CourseAssignmentDTO();
                    assignmentDTO.setId(assignment.getAssignmentId());
                    assignmentDTO.setTitle(assignment.getTitle());
                    assignmentDTO.setScore(studentAssignment.getGrade());
                    assignmentDTO.setDescription(assignment.getDescription());
                    assignmentDTO.setDueDate(assignment.getDeadline().toString());

                    // 使用下载 URL 替代文件路径
                    String downloadUrl = assignment.getCreatedBy() != null
                            ? "/api/downloadattachment/" + assignment.getAssignmentId()
                            : "";
                    assignmentDTO.setAttachment(downloadUrl);
                    //System.out.println(downloadUrl);

                    if (isTeacher) {
                        // 教师查看所有学生提交情况
                        List<SubmissionDTO> submissions = studentAssignmentDAO.getSubmissionsByAssignment(assignment.getAssignmentId()).stream()
                                .map(submission -> {
                                    SubmissionDTO submissionDTO = new SubmissionDTO();
                                    submissionDTO.setStudentId(submission.getStudentId());

                                    User student = null;
                                    try {
                                        student = userDAO.getUserById(submission.getStudentId());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    //System.out.println(submission.getAssignmentId());

                                    submissionDTO.setStudentName(student.getName());
                                    submissionDTO.setScore(submission.getGrade());
                                    submissionDTO.setFile(submission.getSubmissionContent() != null
                                            ? "/api/downloadassignment/" + submission.getAssignmentId()
                                            : "");
                                    return submissionDTO;
                                })
                                .collect(Collectors.toList());
                        assignmentDTO.setSubmissions(submissions);
                    } else {
                        // 学生查看自己提交的作业（返回空的submissions）
                        assignmentDTO.setSubmissions(new ArrayList<>());
                    }

                    return assignmentDTO;
                })
                .collect(Collectors.toList());
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
