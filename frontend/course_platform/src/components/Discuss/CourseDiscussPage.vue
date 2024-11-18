<template>
	<div class="discuss-container-page">
		<h2>课程讨论区 - {{ courseName }}</h2>
		<button class="primary-button" @click="showModal = true">发布帖子</button>

		<ul class="post-list">
			<li class="post-item" v-for="post in posts" :key="post.postid" @click="goToPostDetails(post.postid)">
				<h3 class="post-title">{{ post.title }}</h3>
				<p class="post-meta">作者: {{ post.author }} | 日期: {{ post.time }}</p>
				<button v-if="isTeacher || post.author === currentUser" class="delete-button"
					@click.stop="deletePost(post.postid)">
					删除
				</button>
			</li>
		</ul>

		<Modal v-if="showModal" :visible="showModal" @close="showModal = false">
			<template #header>
				<h3>发布帖子</h3>
			</template>
			<template #body>
				<form class="modal-form" @submit.prevent="submitPost">
					<input v-model="newPost.title" placeholder="标题" required />
					<textarea v-model="newPost.content" placeholder="文字内容" required></textarea>
					<input type="file" id="imgs" multiple accept="image/*" @change="handleFileChange" />
					<p v-if="files.length > 9" class="error">最多上传 9 张图片</p>
				</form>
			</template>
			<template #footer>
				<button class="primary-button" @click="submitPost" :disabled="files.length > 9">发布</button>
			</template>
		</Modal>
		<button style="width: 80px;height: 40px; background-color: darkred;border-radius: 5px; color: white; border: none;" @click="$router.push(`/coursedetail/${this.courseName}`)">
			返回
		</button>
	</div>
</template>

<script>
	import {
		getPostList,
		Post,
		DeletePost,
		fetchUserInfo
	} from '../../api/auth.js';
	import Modal from './Modal.vue';
	export default {
		components: {
			Modal
		},
		data() {
			return {
				courseName: this.$route.params.name,
				posts: [],
				showModal: false,
				newPost: {
					title: '',
					content: ''
				},
				files: [],
				currentUser: '当前用户', // 替换为真实用户名
				isTeacher: false, // 替换为权限判断
			};
		},
		async mounted() {
			try {
				await this.checkUserRole();
				await this.loadPosts();
			} catch (err) {
				console.error(err);
			}
		},
		methods: {
			async loadPosts() {
				this.posts = await getPostList(this.courseName);
			},
			handleFileChange(event) {
				this.files = Array.from(event.target.files);
			},
			async submitPost() {
				const formData = new FormData();
				formData.append('coursename', this.courseName);
				formData.append('title', this.newPost.title);
				formData.append('content', this.newPost.content);
				this.files.forEach((file) => formData.append('imgs', file));
				await Post(formData);
				this.showModal = false;
				this.loadPosts();
			},
			async deletePost(postid) {
				const formdata = {
					coursename: this.courseName,
					postid: postid
				}
				await DeletePost(formdata);
				this.loadPosts();
			},
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
			goToPostDetails(postid) {
				this.$router.push(`/course/${this.courseName}/discuss/${postid}`);
			},
		},
		created() {
			this.loadPosts();
		}
	};
</script>

<style scoped>
/* 容器样式 */
.discuss-container-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
  color: #333;
}

/* 标题样式 */
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #00aa00;
}

/* 按钮样式 */
.primary-button {
  padding: 10px 20px;
  background-color: #00aa00;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.primary-button:hover {
  background-color: #005500;
}

/* 列表样式 */
.post-list {
  list-style: none;
  padding: 0;
}

.post-item {
  width: 90vw;
  background: #f9f9f9;
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background 0.3s;
}

.post-item:hover {
  background: #f0f8ff;
}

.post-title {
  font-size: 18px;
  margin: 0 0 10px;
}

.post-meta {
  font-size: 14px;
  color: #666;
}

/* 删除按钮样式 */
.delete-button {
  padding: 5px 10px;
  background-color: #e74c3c;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.delete-button:hover {
  background-color: #c0392b;
}

/* 模态框表单样式 */
.modal-form input,
.modal-form textarea {
  width: 90%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  
}

.modal-form input[type='file'] {
  border: none;
  padding: 0;
}

.modal-form .error {
  color: #e74c3c;
  font-size: 14px;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: fadeIn 0.3s ease-in-out;
}

.modal-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #00aa00;
  color: white;
}

.modal-body {
  padding: 16px;
}

.modal-footer {
  padding: 16px;
  border-top: 1px solid #eee;
  text-align: right;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
