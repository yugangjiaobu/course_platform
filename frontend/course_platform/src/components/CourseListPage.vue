<template>
  <div id="course-list-page">
    <div id="glass"></div>
    <div class="course-container">
      <h2>课程列表</h2>
      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>
      <div v-if="courses.length">
        <ul class="course-list">
          <li
              v-for="course in courses"
              :key="course.courseListName"
              @click="handleCourseClick(course)"
              class="course-item">
            {{ course.courseListName }}
          </li>
        </ul>
        <div class="home-item" @click="tohome()">back to home</div>
      </div>
      <p v-else>没有找到课程。</p>
    </div>
  </div>
</template>

<script>
import {getCourseList,checkLogin} from '../api/auth.js';


export default {
  data() {
    return {
      courses: [],      // 存储课程列表
      error: '',        // 错误信息
      success: ''       // 成功信息
    };
  },
  methods: {
    handleCourseClick(course) {
      alert(`您点击了课程: ${course.courseListName}`);
      // 可以在这里添加课程详情或进一步操作
    }
    , tohome(){
      this.$router.push('/home');
    },
  },

   async mounted() {
	try{
		// const userstate = await checkLogin();
		// console.log('User State:', userstate);
		 const data= await getCourseList();
		 this.courses=data;
	}catch(err){
		console.error(err);
		// alert('用户未登陆');
		// this.$router.push('/login');
	}
  }
};
</script>

<style scoped>
#course-list-page {
  top: 0;
  left: 0;
  position: fixed;
  width: 100vw;
  height: 100vh;
  background: url('../assets/login.jpg');
  background-repeat: no-repeat;
  background-size: cover;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: green;
  font-family: 'Microsoft YaHei', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
}

h2 {
  margin-left: 2vw;
}

#glass {
  z-index: 1;
  position: absolute;
  background-color: rgba(255, 255, 255, 0.5);
  height: 50vh;
  width: 50vw;
  backdrop-filter: blur(5px);
  border-radius: 2vw;
}

.course-container {
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.course-list {
  list-style: none;
  padding: 0;
  margin: 0;
  width: 40vw;
}

.course-item {
  padding: 10px;
  margin: 10px 0;
  border: 1px solid green;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
}

.course-item:hover {
  background-color: green;
  color: white;
}

.home-item {
  padding: 10px;
  margin: 10px 0;
  border: 1px solid darkred;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  color:darkred;
}

.home-item:hover {
  background-color: red;
  color: white;
}

.success {
  color: green;
}

.error {
  color: red;
}
</style>
