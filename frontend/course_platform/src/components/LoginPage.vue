<template>
	<div id="page">
		<div id="glass">
		</div>
		<div class="login-container">
			
				<h2>欢迎登陆课程平台</h2>
				<form @submit.prevent="loginPage">
				  <div>
				    <label for="username">学号/职工号:</label>
				    <input v-model="username" type="text" id="username" required />
				  </div>
				  <div>
				    <label for="password">密码:</label>
				    <input v-model="password" type="password" id="password" required />
				  </div>
				  <button type="submit">登陆</button>
				</form>
			
			
		  <p v-if="error">{{ error }}</p>
		</div>
	</div>
  
</template>

<script>
import { loginRequest } from '../api/auth.js';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
    };
  },
  methods: {
    async loginPage() {
      try {
        const response = await loginRequest(this.username, this.password);
		window.localStorage.setItem('token',response);
        this.$router.push('/home');//登陆成功跳转到home
      } catch (err) {
		alert('登陆失败');
        console.error('login faild',err);
      }
    },
  },
};
</script>

<style scoped>
#page{
	top:0;
	left: 0;
	position: fixed;
	width:100vw;
	height:100vh;
	background: url('../assets/login.jpg');
	background-repeat: no-repeat;
	background-size: cover;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	color:green;
}
h2{
	margin-left: 2vw;
}
#glass{
	z-index: 1;
	position: absolute;
	background-color: rgba(255, 255, 255 ,0.5);
	height: 50vh;
	width: 50vw;
	backdrop-filter: blur(5px);
	border-radius: 2vw;
	
} 
.login-container{
	z-index: 2;
}
.login-container form{
	height: 40vh;
	width: 50vw;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-around;
}
.login-container form div{
	display: flex;
	justify-content: space-between;
	height: 5vh;
	width: 40vw;
	font-size: 1.2rem;
}
.login-container form div input{
	width: 25vw;
	height: 5vh;
	border: 1px solid green;
	border-radius: 1vh;
}
.login-container form button{
	cursor: pointer;
	width:20vw;
	height: 8vh;
	color: white;
	background-color: darkgreen;
	margin-left: 14vw;
	font-size: 1.3rem;
	border: 1px solid green;
	border-radius: 1vh;
}
.login-container form button:hover{
	background-color: white;
	color: darkgreen;
}
</style>
