<template>
  <form @submit.prevent="submitExam">
    <h2>布置考试</h2>
    <input v-model="exam.coursename" placeholder="课程名称" required />
    <input v-model="exam.examname" placeholder="考试名称" required />
    <input v-model="exam.time" type="datetime-local" required />
    <input v-model="exam.location" placeholder="考试地点" required />
    <select v-model="exam.setstate">
      <option value="0">按学号顺序排列</option>
      <option value="1">随机排列</option>
    </select>
    <textarea v-model="exam.notice" placeholder="注意事项"></textarea>
    <button type="submit">提交</button>
  </form>
</template>

<script>
import { setExam } from "../../api/auth.js";

export default {
  data() {
    return {
      exam: {
        coursename: "",
        examname: "",
        time: "",
        location: "",
        setstate: 0,
        notice: ""
      }
    };
  },
  methods: {
    async submitExam() {
      await setExam(this.exam);
      this.$emit("refresh");
	  alert('布置成功');
    }
  }
};
</script>
<style scoped>
form {
  background: #ffffff;
  border: 1px solid #a5d6a7;
  border-radius: 10px;
  padding: 1.5em;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 100vw;
  margin: 1em auto;
}

h2 {
  text-align: center;
  color: #4caf50;
  margin-bottom: 1em;
}

input, textarea, select {
  width: 100%;
  padding: 0.5em;
  margin: 0.5em 0;
  border: 1px solid #a5d6a7;
  border-radius: 5px;
  font-size: 1em;
}

button {
  display: block;
  width: 100%;
  margin-top: 1em;
  background-color: #4caf50;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  padding: 0.5em 1em;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #66bb6a;
}
</style>

