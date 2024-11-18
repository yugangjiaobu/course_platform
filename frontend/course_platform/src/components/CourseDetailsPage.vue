<template>
  <div id="course-details-page">
	  <button @click="$router.push('/courselist')">返回</button>
    <!-- 课程标题及描述 -->
    <div class="course-header">
      <h2 class="course-name">{{ course.name }}</h2>
      <p class="course-description">{{ course.description }}</p>
    </div>

    <!-- 课程操作按钮 -->
    <div class="course-actions">
      <button class="action-btn" @click="navigateTo('resources')">课程资源</button>
      <button class="action-btn" @click="navigateTo('assignments')">课程作业</button>
      <button class="action-btn" @click="navigateTo('exams')">课程考试</button>
      <button class="action-btn" @click="navigateTo('discuss')">课程讨论</button>
    </div>

    <!-- 教学大纲及其他信息 -->
    <div class="course-info">
      <h3 class="section-title">教学大纲</h3>
      <ul class="syllabus-list">
        <li v-for="(item, index) in syllabus" :key="index" class="syllabus-item">{{ item }}</li>
      </ul>

      <h3 class="section-title">教师信息</h3>
      <div v-if="teacher" class="teacher-info">
        <p>姓名: {{ teacher.name }}</p>
        <p>邮箱: {{ teacher.email }}</p>
      </div>

      <div v-if="isTeacher" class="students-section">
        <h3 class="section-title">选课学生列表</h3>
        <ul class="students-list">
          <li v-for="(student, index) in students" :key="index" class="student-item">
            {{ student.name }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>


<script>
import { getCourseDetails } from '../api/auth.js';

export default {
  data() {
    return {
      course: {
        name:'',
        description:''
      },
      syllabus: [],
      teacher: {
        name:'',
        email:''
      },
      students: [],
      error: '',
      isTeacher: false,
    };
  },
  async mounted() {
    try {
      const course = this.$route.params.course;
      const courseInfo = await getCourseDetails(course);
      console.log(courseInfo);

      this.course.name = courseInfo.courseName;
      this.course.description = courseInfo.courseDescription;
      this.syllabus = courseInfo.syllabus;
      this.teacher.name = courseInfo.teacherName;
      this.teacher.email = courseInfo.teacherEmail;
      this.students = courseInfo.students;
      this.isTeacher = courseInfo.teacher;
    } catch (err) {
      this.error = '加载课程信息时出错，请重试';
      console.error(err);
    }
  },
  methods: {
    navigateTo(page) {
      this.$router.push(`/course/${this.course.name}/${page}`);
    },
  },
};
</script>

<style scoped>
/* 页面整体布局 */
#course-details-page {
width: 95vw;
height: 100vh;
overflow-y: auto;
  padding: 20px;
  background-color: #e8f5e9; /* 浅绿色背景 */
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 柔和阴影 */
  font-family: Arial, sans-serif;
  color: #333; /* 深色字体 */
}

/* 标题样式 */
.course-header {
  text-align: center;
  margin-bottom: 20px;
}

.course-name {
  font-size: 1.8em;
  color: #4caf50; /* 绿色标题 */
}

.course-description {
  font-size: 1em;
  color: #666; /* 次要文本灰色 */
}

/* 操作按钮 */
.course-actions {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30px;
}

.action-btn {
  background-color: #4caf50; /* 按钮绿色背景 */
  color: #fff; /* 按钮文字白色 */
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.action-btn:hover {
  background-color: #388e3c; /* 鼠标悬浮时的深绿色 */
  transform: scale(1.05);
}

/* 信息块样式 */
.course-info {
  padding: 10px 20px;
}

.section-title {
  font-size: 1.5em;
  color: #4caf50; /* 绿色小标题 */
  margin-bottom: 10px;
  border-bottom: 2px solid #4caf50; /* 绿色下划线 */
  display: inline-block;
  padding-bottom: 5px;
}

/* 教学大纲 */
.syllabus-list {
  list-style-type: disc;
  padding-left: 20px;
  margin-bottom: 20px;
}

.syllabus-item {
  margin-bottom: 5px;
  font-size: 1em;
  color: #333; /* 深色文本 */
}

/* 教师信息 */
.teacher-info {
  background-color: #fff; /* 白色背景 */
  padding: 15px;
  border: 1px solid #ddd; /* 浅灰边框 */
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 柔和阴影 */
  margin-bottom: 20px;
}

.teacher-info p {
  margin: 5px 0;
}

/* 学生列表 */
.students-section {
  margin-top: 20px;
}

.students-list {
  list-style-type: none;
  padding-left: 0;
}

.student-item {
  padding: 10px;
  border-bottom: 1px solid #ddd; /* 浅灰色分隔线 */
  font-size: 1em;
  color: #333; /* 深色字体 */
}
</style>


