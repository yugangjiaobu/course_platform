<template>
  <div id="course-details-page">
    <div class="course-header">
      <h2>{{ course.name }}</h2>
      <p>{{ course.description }}</p>
    </div>
	<div class="course-actions">
	  <button @click="navigateTo('resources')">课程资源</button>
	  <button @click="navigateTo('assignments')">课程作业</button>
	  <button @click="navigateTo('exams')">课程考试</button>
	  <button @click="navigateTo('discuss')">课程讨论</button>
	</div>
    <div class="course-info">
      <h3>教学大纲</h3>
      <ul>
        <li v-for="(item, index) in syllabus" :key="index">{{ item }}</li>
      </ul>

      <h3>教师信息</h3>
      <div v-if="teacher">
        <p>姓名: {{ teacher.name }}</p>
        <p>邮箱: {{ teacher.email }}</p>
      </div>

      <div v-if="isTeacher">
        <h3>选课学生列表</h3>
        <ul>
          <li v-for="(student, index) in students" :key="index">{{ student.name }}</li>
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
      course: {},
      syllabus: [],
      teacher: null,
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

      this.course = courseInfo.course;
      this.syllabus = courseInfo.syllabus;
      this.teacher = courseInfo.teacher;
      this.students = courseInfo.students;
      this.isTeacher = courseInfo.isTeacher;
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
html, body {
  height: 100%; /* 使html和body充满视口 */
  width: 100vw;
  margin: 0; /* 去掉默认边距 */
}

#course-details-page {
  padding: 20px;
  font-family: 'Microsoft YaHei', sans-serif;
  height: calc(100vh - 40px); /* 减去上下的内边距以适应视口 */
   width: calc(100vw - 40px);
  overflow: auto; /* 允许滚动 */
}

.course-header {
  text-align: center;
  margin-bottom: 20px;
}

.course-info h3 {
  margin-top: 20px;
}

.course-actions {
  position: relative; /* 改为相对定位 */
  display: flex;
  flex-wrap: wrap; /* 允许按钮换行 */
  justify-content: center; /* 居中对齐 */
  margin-top: 30px;
  padding: 10px 0; /* 增加内边距以避免按钮与边缘重叠 */
}

button {
  padding: 10px 20px;
  border: none;
  background-color: green;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin: 5px; /* 增加边距以防止挤压 */
}

button:hover {
  background-color: darkgreen;
}

/* 添加响应式样式 */
@media (max-width: 600px) {
  .course-actions {
    flex-direction: column; /* 在小屏幕上垂直排列 */
    align-items: center; /* 居中对齐 */
  }
}
</style>
