<template>
  <div id="page">
	
    <div id="glass"></div>
    <div class="info-container">
      <h2>个人信息管理</h2>
	  
      <form @submit.prevent="updateInfo">
        <div>
          <label for="username">姓名:</label>
          <span>{{ username }}</span>
        </div>
        <div>
          <label for="userid">学号/职工号:</label>
          <span>{{ userid }}</span>
        </div>
        <div>
          <label for="role">身份:</label>
          <span>{{ role }}</span>
        </div>
        <div>
          <label for="email">电子邮件:</label>
          <input v-model="email" type="email" id="email" required />
        </div>
        <div>
          <label for="phone">联系电话:</label>
          <input v-model="phone" type="tel" id="phone" required />
        </div>
        <button type="submit">更新信息</button>
		<button @click="$router.back()">返回</button>
      </form>
      <p v-if="error">{{ error }}</p>
      <p v-if="success">{{ success }}</p>

      <h3>我点赞的帖子</h3>
      <ul class="list">
        <li v-for="post in likeList" :key="post.postid" @click="goToPost(post.coursename, post.postid)">
          <h4>{{ post.title }}</h4>
          <p>课程: {{ post.coursename }} | 作者: {{ post.author }} | 时间: {{ post.time }}</p>
        </li>
      </ul>

      <h3>我发布的帖子</h3>
      <ul class="list">
        <li v-for="post in myPostList" :key="post.postid" @click="goToPost(post.coursename, post.postid)">
          <h4>{{ post.title }}</h4>
          <p>课程: {{ post.coursename }} | 时间: {{ post.time }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {  fetchUserInfo, updateInfo, getLikePost, getMyPost } from "../api/auth.js";

export default {
  data() {
    return {
      username: "xxx", // 默认值
      userid: "id",
      email: "example@example.com", // 默认值
      phone: "123456789", // 默认值
      role: "",
      likeList: [], // 点赞帖子列表
      myPostList: [], // 发布帖子列表
      error: "",
      success: "",
    };
  },
  methods: {
    async fetchinfo() {
      try {
        const userInfo = await fetchUserInfo();
        if (userInfo) {
          this.email = userInfo.email;
          this.phone = userInfo.tel;
          this.username = userInfo.name;
          this.userid = userInfo.id;
          this.role = userInfo.role;
        } else {
          this.error = "获取用户信息失败";
        }
      } catch (error) {
        this.error = "服务器错误";
      }
    },
    async updateInfo() {
      try {
        const updateres = await updateInfo(this.email, this.phone);
        if (updateres) {
          this.success = "更新信息成功";
        } else {
          this.error = "更新用户信息失败";
        }
      } catch (error) {
        this.error = "服务器错误";
      }
    },
    async fetchPostLists() {
      try {
        const likes = await getLikePost();
        const myPosts = await getMyPost();
        this.likeList = likes || [];
        this.myPostList = myPosts || [];
      } catch (error) {
        this.error = "获取帖子列表失败";
      }
    },
    goToPost(course, postid) {
      this.$router.push(`/course/${course}/discuss/${postid}`);
    },
  },
  async mounted() {
    try {
      await this.fetchinfo();
      await this.fetchPostLists();
    } catch (err) {
      console.error(err);
      alert("用户未登陆");
      this.$router.push("/login");
    }
  },
};
</script>
<style scoped>
@charset "UTF-8";

/* 确保全局滚动可用 */
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  font-family: "Microsoft YaHei", sans-serif;
  background-color: #d8ffbe;
}

/* 页面主容器 */
#page {
  width: 100vw;
  min-height: 100vh;
  overflow-y: auto;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
  background: linear-gradient(to bottom right, #d8ffc0, #55ff00);
}

/* 背景半透明覆盖层 */
#glass {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(8px);
  z-index: 0;
}
/* 信息容器样式 */
.info-container {
  z-index: 1;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  max-width: 80%;
  width: 100%;
  overflow: hidden;
}

/* 标题样式 */
h2 {
  margin-bottom: 20px;
  color: #88c662;
  font-size: 1.8rem;
  text-align: center;
}

/* 表单样式 */
form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
}

.form-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

label {
  flex: 1;
  font-size: 1rem;
  color: #333;
}

input {
  flex: 2;
  padding: 8px 10px;
  border: 1px solid #88c662;
  border-radius: 5px;
  font-size: 1rem;
}

button {
  padding: 10px 15px;
  font-size: 1rem;
  color: white;
  background-color: #88c662;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #619a0b;
}

/* 错误与成功信息 */
.error-message {
  color: red;
  font-weight: bold;
  text-align: center;
}

.success-message {
  color: green;
  font-weight: bold;
  text-align: center;
}

/* 列表样式 */
.list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list li {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 10px;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.list li:hover {
  background-color: #e0f2f1;
}

/* 超出时滚动 */
.info-container {
  max-height: 80vh; /* 确保容器不会超出视口 */
  overflow-y: auto;
}

</style>
