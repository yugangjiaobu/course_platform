<template>
  <div>
    <ul>
      <li v-for="exam in exams" :key="exam.examname">
        <h3>{{ exam.examname }}</h3>
        <p>地点: {{ exam.location }}</p>
        <p>时间: {{ exam.time }}</p>
        <p v-if="role === 'student'">座位号: {{ exam.setid }} | 成绩: {{ exam.score }}</p>
        <p v-if="role === 'teacher'">注意事项: {{ exam.notice }}</p>
        
        <template v-if="role === 'teacher'">
          <button @click="deleteExam(exam)">删除考试</button>
          <button @click="openScoreModal(exam)">学生分数管理</button>
        </template>
      </li>
    </ul>
    <ExamScoreModal v-if="currentExam" :exam="currentExam" @close="closeScoreModal" @refresh="refresh" />
  </div>
</template>

<script>
import { delExam } from "../../api/auth.js";
import ExamScoreModal from "./ExamScoreModal.vue";

export default {
  props: ["exams", "role"],
  components: { ExamScoreModal },
  data() {
    return {
      currentExam: null
    };
  },
  methods: {
    async deleteExam(exam) {
      await delExam({ coursename: exam.coursename, examname: exam.examname });
      this.$emit("refresh");
    },
    openScoreModal(exam) {
      this.currentExam = exam;
    },
    closeScoreModal() {
      this.currentExam = null;
    }
  }
};
</script>
<style scoped>
ul {
  padding: 1em;
  background: #ffffff;
  border: 1px solid #a5d6a7;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

li {
  border-bottom: 1px solid #e0e0e0;
  padding: 1em 0;
}

li:last-child {
  border-bottom: none;
}

h3 {
  font-size: 1.2em;
  color: #2e7d32;
}

p {
  margin: 0.5em 0;
}

button {
  background-color: #4caf50;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  padding: 0.5em 1em;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 1em;
}

button:hover {
  background-color: #66bb6a;
}
</style>

