<template>
  <div id="page">
    <div id="glass"></div>
    <div class="info-container">
      <h2>个人信息管理</h2>
      <form @submit.prevent="updateInfo">
        <div>
          <label for="username">学号/职工号:</label>
          <span>{{ username }}</span> <!-- 修改为只读文本 -->
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
      </form>
      <p v-if="error">{{ error }}</p>
      <p v-if="success">{{ success }}</p>
    </div>
  </div>
</template>


<script>
// import { updateUserInfo } from '../api/user.js';
// import { getUserInfo } from '../api/user.js';

export default {
  data() {
    return {
      username: '20210001', // 默认值
      email: 'example@example.com', // 默认值
      phone: '123456789', // 默认值
      error: '',
      success: '',
    };
  },
  methods: {
    async fetchUserInfo() {
      try {
        const response = await fetch(`/api/user/${this.username}`);
        if (response.ok) {
          const userInfo = await response.json();
          this.email = userInfo.email;
          this.phone = userInfo.phone;
        } else {
          this.error = '获取用户信息失败';
        }
      } catch (error) {
        this.error = '服务器错误';
      }
    },
    updateInfo() {
      // 模拟更新操作
      if (this.username && this.email && this.phone) {
        this.success = '信息更新成功！';
        this.error = '';
      } else {
        this.error = '更新失败，请检查信息。';
        this.success = '';
      }
     },
    },
    // 在组件挂载时获取用户信息
    mounted() {
       this.fetchUserInfo(); // 你可以实现一个函数来加载用户信息
    }
};
</script>

<style scoped>
/* 确保字符编码为 UTF-8 */
@charset "UTF-8";

#page {
  top: 0;
  left: 0;
  position: fixed;
  width: 100vw;
  height: 100vh;
  background: url('../assets/login.jpg'); /* 使用个人信息管理背景 */
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
.info-container {
  z-index: 2;
}
.info-container form {
  height: 50vh;
  width: 50vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
}
.info-container form div {
  display: flex;
  justify-content: space-between;
  height: 5vh;
  width: 40vw;
  font-size: 1.2rem;
}
.info-container form input {
  width: 25vw;
  height: 5vh;
  border: 1px solid green;
  border-radius: 1vh;
}
.info-container form button {
  cursor: pointer;
  width: 20vw;
  height: 8vh;
  color: darkgreen;
  margin-left: 14vw;
  font-size: 1.3rem;
  border: 1px solid green;
  border-radius: 1vh;
}
.info-container form button:hover {
  background-color: green;
  color: white;
.success {
  color: white; /* 设置成功消息的字体颜色为白色 */
}
}
</style>
