<template>
  <div>
    <h1>考试管理</h1>
	<button @click="$router.back()">返回</button>
    <ExamList :exams="exams" :role="role" @refresh="fetchExams" />
    <template v-if="role === 'teacher'">
      <ExamForm @refresh="fetchExams" />
    </template>
  </div>
</template>

<script>
import { getExam,fetchUserInfo } from '../../api/auth.js';
import ExamList from "./ExamList.vue";
import ExamForm from "./ExamForm.vue";

export default {
	components: {
	  ExamList,
	  ExamForm,
	},
  data() {
    return {
	courseName: this.$route.params.name,
      exams: [],
      role: "" // 用户身份，"student" 或 "teacher"
    };
  },
  methods: {
    async fetchExams() {
      const data = await getExam(this.courseName);
      this.exams = data;
    },
	async checkUserRole() {
		try {
			const res = await fetchUserInfo();
			this.role=res.role;
		} catch (err) {
			console.error('Failed to fetch user info:', err);
		}
	},
  },
  async mounted() {
  	try {
  		await this.checkUserRole();
  		await this.fetchExams();
  	} catch (err) {
  		console.error(err);
  	}
  },
};
</script>
<style scoped>
div {
  max-width: 100vw;
  max-height: 100vh;
  overflow: auto;
  padding: 1em;
  background: #f4fdf4;
  border-radius: 10px;
}

h1 {
  text-align: center;
  color: #4caf50;
  margin-bottom: 1em;
}

template > div {
  display: flex;
  flex-direction: column;
  gap: 1em;
}

.ExamList, .ExamForm {
  margin: 1em 0;
}
</style>

