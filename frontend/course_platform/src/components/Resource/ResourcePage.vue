<template>
	<div id="resource-page">
		<div id="glass"></div>
		<div class="resource-container">
			<h2>课程资源</h2>
			<div v-if="error" class="error">{{ error }}</div>
			<div v-if="success" class="success">{{ success }}</div>

			<!-- 如果是老师，显示上传资源区域 -->
			<div v-if="isTeacher">
				<input v-model="newResourceName" placeholder="资源名称" />
				<input type="file" @change="handleFileChange" />
				<button @click="upload">上传资源</button>
			</div>

			<!-- 资源列表 -->
			<div v-if="resources.length">
        <ul class="resource-list">
          <li v-for="(resource, index) in fullResourceUrls" :key="index" class="resource-item">
            <a :href="resource.fullUrl" download>{{ resource.name }}</a>
            <button v-if="isTeacher" @click="deleteResource(index)">删除</button>
          </li>
        </ul>
			</div>
			<p v-else>没有资源。</p>

			<div class='back-button' @click="backToHome()">返回</div>
		</div>
	</div>
</template>

<script>
	import {
		getResource,
		uploadResource,
		deleteResource,
		fetchUserInfo
	} from '../../api/auth.js';

	export default {
		data() {
			return {
				resources: [], // 资源列表
				newResourceName: '', // 新资源名称
				selectedFile: null, // 选中文件
				error: '', // 错误信息
				success: '', // 成功信息
				isTeacher: false // 是否为老师
			};
		},
    computed: {
      fullResourceUrls() {
        return this.resources.map(resource => ({
          ...resource,
          fullUrl: `http://localhost:8000${resource.url}`
        }));
      }
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
			handleFileChange(event) {
				this.selectedFile = event.target.files[0];
			},
			async upload() {
				if (!this.newResourceName || !this.selectedFile) {
					this.error = '资源名称和文件不能为空';
					return;
				}

				// 从路由获取课程名称
				const courseName = this.$route.params.name;
				try {
					await uploadResource(courseName,this.selectedFile,this.newResourceName);
					this.success = '资源上传成功';
					this.resources.push({ name: this.newResourceName, url: `/uploads/${this.selectedFile.name}` });
					this.newResourceName = '';
					this.selectedFile = null;
				} catch (err) {
					this.error = '资源上传失败';
					console.error(err);
				}
			},
			async deleteResource(index) {
				const resource = this.resources[index];
				try {
					await deleteResource(resource.id);
					this.resources.splice(index, 1);
					this.success = '资源删除成功';
				} catch (err) {
					this.error = '资源删除失败';
					console.error(err);
				}
			},
			backToHome() {
				const course=this.$route.params.name;
				this.$router.push(`/coursedetail/${course}`);
			}
		},
		async mounted() {
			await this.checkUserRole(); // 调用 checkUserRole 方法
			try {
				const coursename=this.$route.params.name;
				const data = await getResource(coursename);
				this.resources = data;
			} catch (err) {
				console.error('Failed to load resources:', err);
			}
		}
	};
</script>
<style scoped>
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
#resource-page {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
  font-family: 'Microsoft YaHei', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
  color: #2e7d32;
}

.resource-container {
  background: #f9f9f9;
  padding: 2em;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 90vw;
  max-width: 700px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  margin-bottom: 1em;
  color: #2e7d32;
  font-size: 1.5em;
  text-align: center;
}

.upload-section {
  margin-bottom: 20px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.resource-list {
  width: 100%;
  max-height: 50vh;
  overflow-y: auto;
  margin: 1em 0;
  padding: 0;
  list-style: none;
}

.resource-item {
  background: #e8f5e9;
  color: #2e7d32;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #c8e6c9;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.delete-button {
  margin-left: 10px;
  color: #d32f2f;
  cursor: pointer;
  transition: color 0.3s ease;
}

.delete-button:hover {
  color: #b71c1c;
}

a {
  color: #2e7d32;
  text-decoration: none;
  font-weight: bold;
}

a:hover {
  text-decoration: underline;
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

.back-button {
  padding: 10px;
  margin-top: 20px;
  border: 1px solid #d32f2f;
  border-radius: 5px;
  color: #d32f2f;
  cursor: pointer;
  width: 100%;
  text-align: center;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.back-button:hover {
  background-color: #d32f2f;
  color: white;
}
</style>

