package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.dto.*;
import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.datatransfer.StringSelection;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExampaperServiceImpl implements ExampaperService {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private ExampaperDAO exampaperDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ExaminersChoiceDAO examinersChoiceDAO;

    @Override
    public void createExampaperTest(SetTestRequest request) throws SQLException {
        // 获取课程信息
        Course course = courseDAO.getCourseByName(request.getCoursename());
        if (course == null) {
            throw new SQLException("Course not found");
        }

        String courseId = course.getCourseId();

        String notbutExamId = UUID.randomUUID().toString();

        // 遍历选择题数据并保存到数据库
        for (int i = 0; i < request.getChoices().size(); i++) {
            SetTestRequest.Choice choice = request.getChoices().get(i);

            // 构建 Exampaper 实体
            Exampaper exampaper = new Exampaper();
            exampaper.setPaperId(UUID.randomUUID().toString());
            exampaper.setNotbutExamId(notbutExamId);
            exampaper.setPaperContent(choice.getTitle());
            exampaper.setQuestionNum(i + 1);
            exampaper.setAnswerChoice(String.valueOf(choice.getRight()));
            exampaper.setChoiceA(choice.getOptions().get(0));
            exampaper.setChoiceB(choice.getOptions().get(1));
            exampaper.setChoiceC(choice.getOptions().get(2));
            exampaper.setChoiceD(choice.getOptions().get(3));
            exampaper.setCourseId(courseId);
            exampaper.setTestName(request.getTestname());
            exampaper.setDeadline(request.getDeadline());

            // 保存选择题数据
            exampaperDAO.save(exampaper);
        }
    }

    @Override
    public List<TestResponseDTO> getTestList(String courseName, String userRole, String userId) throws SQLException {
        List<TestResponseDTO> responseDTOs = new ArrayList<>();

        // 获取课程信息
        Course course = courseDAO.getCourseByName(courseName);
        if (course == null) {
            throw new SQLException("Course not found");
        }

        // 获取与课程相关的所有测验
        List<Exampaper> exampapers = exampaperDAO.findByCourseId(course.getCourseId());

        // 按照 notbut_exam_id 聚合测验
        Map<String, TestResponseDTO> testMap = new HashMap<>();

        for (Exampaper exampaper : exampapers) {
            String notbutExamId = exampaper.getNotbutExamId();

            // 如果不存在，则创建一个新的 TestResponseDTO
            TestResponseDTO dto = testMap.get(notbutExamId);
            if (dto == null) {
                dto = new TestResponseDTO();
                dto.setTestname(exampaper.getTestName());
                dto.setDeadline(exampaper.getDeadline());

                // 如果是学生，返回学生的成绩
                if ("student".equals(userRole)) {
                    Integer score = examinersChoiceDAO.getScoreByExamIdAndUserId(exampaper.getNotbutExamId(), userId);
                    dto.setScore(score);  // 如果没有成绩，返回 null
                }
                // 如果是教师，返回学生成绩列表
                else if ("teacher".equals(userRole)) {
                    List<TestResponseDTO.StudentScore> studentScores = new ArrayList<>();
                    List<ExaminersChoice> examinersChoices = examinersChoiceDAO.findByExamId(exampaper.getNotbutExamId());
                    for (ExaminersChoice ec : examinersChoices) {
                        Integer score = ec.getScore();
                        // 如果成绩为 null，则为其设置一个默认值（例如：0）
                        TestResponseDTO.StudentScore studentScore = new TestResponseDTO.StudentScore(
                                ec.getName(), score != null ? score : 0);  // 处理 null 情况
                        studentScores.add(studentScore);
                    }
                    dto.setStudentScores(studentScores);
                }

                testMap.put(notbutExamId, dto);
            }

            // 将选择题加入到 TestResponseDTO 中
            TestResponseDTO.Question question = new TestResponseDTO.Question();
            question.setPaperId(exampaper.getPaperId());
            question.setChoiceA(exampaper.getChoiceA());
            question.setChoiceB(exampaper.getChoiceB());
            question.setChoiceC(exampaper.getChoiceC());
            question.setChoiceD(exampaper.getChoiceD());

            dto.getQuestions().add(question);
        }

        // 返回合并后的所有测验
        responseDTOs.addAll(testMap.values());
        return responseDTOs;
    }

    @Override
    public void uploadTestAnswers(String coursename, String testname, List<UploadTestAnswerDTO.AnswerDTO> answers, String userId) throws Exception {
        // 获取课程对象
        Course course = courseDAO.getCourseByName(coursename);

        User user = userDAO.getUserById(userId);

        // 查询该课程和测试名称的所有试卷
        List<Exampaper> exampapers = exampaperDAO.findExampaperByCourseAndTestname(course.getCourseId(), testname);

        if (exampapers.isEmpty()) {
            throw new SQLException("Test not found");
        }

        // 用一个映射存储每道选择题的正确答案，以 questionNum 为键
        Map<Integer, String> correctAnswersMap = new HashMap<>();
        Map<Integer, String> examNumMap = new HashMap<>();

        for (Exampaper exampaper : exampapers) {
            correctAnswersMap.put(exampaper.getQuestionNum(), exampaper.getAnswerChoice());
            examNumMap.put(exampaper.getQuestionNum(), exampaper.getAnswerChoice());
        }

        // 学生选择的答案和分数初始化
        int totalScore = 0;
        StringBuilder stuChoiceBuilder = new StringBuilder();

        // 遍历学生的答案
        for (UploadTestAnswerDTO.AnswerDTO answer : answers) {
            int questionNum = answer.getId();
            String studentAnswer = String.valueOf(answer.getAnswer());

            // 查找该题的正确答案
            String correctAnswer = correctAnswersMap.get(questionNum);
            if (correctAnswer != null) {
                // 判断学生的答案是否正确
                if (correctAnswer.equalsIgnoreCase(studentAnswer)) {
                    totalScore += 3; // 每道选择题得分 3 分
                }
            }

            // 保存学生的选择答案，按顺序
            if (stuChoiceBuilder.length() > 0) {
                stuChoiceBuilder.append(",");
            }
            stuChoiceBuilder.append(studentAnswer);

        }


        String examNum = UUID.randomUUID().toString();
        String notbutExamId = exampaperDAO.getNotbutExamIdByTestname(testname);
        String stuChoice = stuChoiceBuilder.toString();

        examinersChoiceDAO.saveChoiceNumForStudent(user.getName(), examNum, notbutExamId, userId, stuChoice, totalScore, null);
    }



    @Override
    public List<ExampaperDetailDTO> getTestDetails(String coursename, String testname, String user_id) throws Exception {
        // 获取课程信息
        Course course = courseDAO.getCourseByName(coursename);
        if (course == null) {
            throw new SQLException("Course not found");
        }

        User user = userDAO.getUserById(user_id);

        // 查询与课程名和测验名相关的选择题
        List<Exampaper> exampapers = exampaperDAO.findByCourseIdAndTestname(course.getCourseId(), testname);
        if (CollectionUtils.isEmpty(exampapers)) {
            throw new SQLException("No test questions found for this course and test");
        }

        // 随机打乱选择题顺序
        Collections.shuffle(exampapers);

        List<ExampaperDetailDTO> testDetails = new ArrayList<>();
        StringBuilder choiceNumBuilder = new StringBuilder();  // 用于存储打乱后的题号顺序
        for (Exampaper exampaper : exampapers) {
            ExampaperDetailDTO dto = new ExampaperDetailDTO();
            dto.setTitle(exampaper.getPaperContent());  // 题目内容
            dto.setId(exampaper.getQuestionNum());     // 题号
            dto.setOptions(Arrays.asList(               // 选择题选项
                    exampaper.getChoiceA(),
                    exampaper.getChoiceB(),
                    exampaper.getChoiceC(),
                    exampaper.getChoiceD()
            ));
            testDetails.add(dto);

            // 将题号添加到打乱顺序的题号列表中
            if (choiceNumBuilder.length() > 0) {
                choiceNumBuilder.append(",");  // 题号之间用逗号分隔
            }
            choiceNumBuilder.append(exampaper.getQuestionNum());
        }

        return testDetails;
    }

}
