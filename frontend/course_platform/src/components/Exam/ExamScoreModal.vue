<template>
  <div class="modal">
    <h3>上传分数 - {{ exam.examname }}</h3>
    <ul>
      <li v-for="student in exam.students" :key="student.id">
        {{ student.name }} ({{ student.id }}) - 当前成绩: {{ student.score || '无' }}
        <input v-model="studentScores[student.id]" type="number" placeholder="输入分数" min="0" max="100"/>
      </li>
    </ul>
    <button @click="uploadScores">上传分数</button>
    <button @click="$emit('close')">关闭</button>
  </div>
</template>

<script>
import { uploadexamscore } from "../../api/auth.js";

export default {
  props: ["exam"],
  data() {
    return {
      studentScores: {}
    };
  },
  methods: {
    async uploadScores() {
      const students = Object.keys(this.studentScores).map((id) => ({
        sid: id,
        score: this.studentScores[id]
      }));
      await uploadexamscore({ coursename: this.exam.coursename, examname: this.exam.examname, students });
	  this.$emit("refresh");
      this.$emit("close");
    }
  }
};
</script>
<style scoped>
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #ffffff;
  border: 1px solid #a5d6a7;
  border-radius: 10px;
  padding: 2em;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  max-width: 90vw;
  width: 90%;
  overflow: auto;
}

h3 {
  margin-bottom: 1em;
  color: #4caf50;
  text-align: center;
}

ul {
  margin: 1em 0;
  padding: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0.5em 0;
}

input {
  width: 60%;
  padding: 0.5em;
  border: 1px solid #a5d6a7;
  border-radius: 5px;
  font-size: 1em;
}

button {
  margin-top: 1em;
  padding: 0.5em 1em;
  background-color: #4caf50;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 1em;
}

button:hover {
  background-color: #66bb6a;
}
</style>

