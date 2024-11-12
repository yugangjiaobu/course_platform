<template>
	<div id="notification-page">
		<div id="glass"></div>
		<div class="notification-container">
			<h2>通知</h2>
			<div v-if="error" class="error">{{ error }}</div>
			<div v-if="success" class="success">{{ success }}</div>

			<!-- 如果是老师，显示发送通知区域 -->
			<div v-if="isTeacher">
				<textarea v-model="newNotification" placeholder="输入通知内容"></textarea>
				<button @click="sendNotification">发送通知</button>
			</div>

			<!-- 通知列表 -->
			<div v-if="notifications.length">
				<ul class="notification-list">
					<li v-for="(notification, index) in notifications" :key="index" class="notification-item">
						{{ notification }}
					</li>
				</ul>
			</div>
			<p v-else>没有通知。</p>

			<div class='back-button' @click="backToHome()">返回首页</div>
		</div>
	</div>
</template>

<script>
	import {
		getNotifications,
		sendNotification,
		fetchUserInfo
	} from '../api/auth.js';

	export default {
		data() {
			return {
				notifications: [], // 通知列表
				newNotification: '', // 新通知内容
				error: '', // 错误信息
				success: '', // 成功信息
				isTeacher: false // 是否为老师
			};
		},
		methods: {
			async checkUserRole() {
				try {
					const res = await fetchUserInfo();
					if (res.role === 'teacher') {
						this.isTeacher = true;
					}
				} catch (err) {
					console.error('Failed to fetch user info:', err);
				}
			},
			async sendNotification() {
				if (!this.newNotification) {
					this.error = '通知内容不能为空';
					return;
				}
				try {
					await sendNotification(this.newNotification);
					this.success = '通知发送成功';
					this.notifications.push(this.newNotification);
					this.newNotification = '';
				} catch (err) {
					this.error = '发送通知失败';
					console.error(err);
				}
			},
			backToHome() {
				this.$router.push('/home');
			}
		},
		async mounted() {
			await this.checkUserRole(); // 调用 checkUserRole 方法
			try {
				const data = await getNotifications();
				this.notifications = data;
			} catch (err) {
				console.error('Failed to load notifications:', err);
			}
		}
	};
</script>

<style scoped>
	*,
	*::before,
	*::after {
	    box-sizing: border-box; /* 统一设置 box-sizing */
	    margin: 0;
	    padding: 0;
	}
	#notification-page {
		position: fixed;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background: rgba(255, 255, 255, 0.9);
		display: flex;
		justify-content: center;
		align-items: center;
		font-family: 'Microsoft YaHei', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
		color: #2e7d32;
		/* 统一绿色色调 */
	}

	.notification-container {
		background: #f9f9f9;
		padding: 2em;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
		width: 80vw;
		max-width: 800px;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	h2 {
		margin-bottom: 1em;
		color: #2e7d32;
		font-size: 1.5em;
	}

	.notification-list {
		width: 80vw;
		max-height: 40vh;
		overflow-y: auto;
		margin: 1em 0;
		padding: 0;
		list-style: none;
	}

	.notification-item {
		background: #e8f5e9;
		color: #2e7d32;
		padding: 10px;
		margin-bottom: 10px;
		border: 1px solid #c8e6c9;
		border-radius: 5px;
		text-align: center;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

	textarea {
		width: 100%;
		height: 70px;
		margin-bottom: 15px;
		padding: 10px;
		border: 1px solid #c8e6c9;
		border-radius: 5px;
		resize: none;
		box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
	}

	button {
		padding: 10px 20px;
		background-color: #2e7d32;
		color: white;
		border: none;
		border-radius: 5px;
		cursor: pointer;
		font-weight: bold;
		transition: background-color 0.3s ease;
		width: 100%;
	}

	button:hover {
		background-color: #1b5e20;
	}

	.back-button {
		padding: 10px;
		margin-top: 20px;
		border: 1px solid #d32f2f;
		border-radius: 5px;
		color: #d32f2f;
		cursor: pointer;
		text-align: center;
		width: 100%;
		transition: background-color 0.3s ease, color 0.3s ease;
	}

	.back-button:hover {
		background-color: #d32f2f;
		color: white;
	}

	.success,
	.error {
		font-weight: bold;
		margin: 10px 0;
		text-align: center;
	}

	.success {
		color: #388e3c;
	}

	.error {
		color: #d32f2f;
	}
</style>