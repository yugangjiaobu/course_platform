<template>
	<div id="assignments-page">
		<h2 class="page-title">{{ isTeacher ? '教师端 | 作业管理' : '学生端 | 作业列表' }}</h2>
		<button style="margin-bottom: 10px;" @click="$router.push(`/coursedetail/${this.$route.params.name}`)">返回</button>
		<div class="assignments-section">
			<!-- 教师端视图 -->
			<div v-if="isTeacher">
				<button class="create-btn" @click="showCreateModal">新建作业</button>
				<ul class="assignments-list">
					<li v-for="assignment in assignments" :key="assignment.id" class="assignment-item teacher-view"
						@click="viewStudentSubmissions(assignment)">
						<h4 class="assignment-title">
							{{ assignment.title }}
							<span class="due-date">- 截止日期: {{ assignment.dueDate }}</span>
						</h4>
						<p class="assignment-description">{{ assignment.description }}</p>
						<div v-if="assignment.attachment" class="attachment-link">
							<a :href="assignment.attachment" download>下载附件</a>
						</div>
					</li>
				</ul>
			</div>

			<!-- 学生端视图 -->
			<div v-else>
				<ul class="assignments-list">
					<li v-for="assignment in assignments" :key="assignment.id" class="assignment-item student-view"
						@click="viewAssignment(assignment)">
						<h4 class="assignment-title">
							{{ assignment.title }}
							<span class="due-date">- {{ assignment.dueDate }}</span>
						</h4>
						<p>分数: {{ assignment.score || '未评分' }}</p>
					</li>
				</ul>
			</div>
		</div>

		<!-- 作业详情模态框 -->
		<modal v-if="showModal" @close="showModal = false">
			<template #header>
				<h3>{{ modalAssignment.title }}</h3>
			</template>
			<template #body>
				<p>{{ modalAssignment.description }}</p>
				<div v-if="modalAssignment.attachment" class="attachment-link">
					<a :href="modalAssignment.attachment" download>下载附件</a>
				</div>
				<input v-if="!isPastDueDate" type="file" @change="handleFileUpload" class="file-upload" />
			</template>
			<template #footer>
				<button v-if="!isPastDueDate" class="submit-btn" @click="submitAssignment">
					提交作业
				</button>
				<span v-else class="deadline-message">作业已截至</span>
			</template>
		</modal>

		<!-- 学生提交情况模态框 -->
		<modal v-if="showStudentSubmissionsModal" @close="showStudentSubmissionsModal = false">
			<template #header>
				<h3>{{ currentAssignment.title }} 学生提交情况</h3>
			</template>
			<template #body>
				<ul class="submissions-list">
					<li v-for="submission in currentAssignment.submissions" :key="submission.studentId"
						class="submission-item">
						{{ submission.studentName }} -
						<span v-if="submission.file">
							<a :href="submission.file" download>下载作业</a>
						</span>
						<span v-else>未提交</span>
						- 分数: {{ submission.score || '未评分' }}
						<div class="grading-section">
							<input type="number" v-model.number="submission.inputScore" min="0" max="100"
								placeholder="输入分数" class="score-input" />
							<button class="grade-btn"
								@click="gradeAssignment(submission.studentId, currentAssignment.id, submission.inputScore)">
								判分
							</button>
						</div>
					</li>
				</ul>
			</template>
		</modal>

		<!-- 新建作业模态框 -->
		<create-assignment-modal v-if="this.showCreateAssignmentModal" @close="this.showCreateAssignmentModal = false"
			@create="fetchAssignments" />
	</div>
</template>


<script>
	import {
		getAssignMent,
		fetchUserInfo,
		uploadAssignMent,
		submitScore
	} from '../../api/auth.js';
	import Modal from './Modal.vue';
	import CreateAssignmentModal from './CreateAssignmentModal.vue';

	export default {
		components: {
			Modal,
			CreateAssignmentModal,
		},
		data() {
			return {
				courseName: '',
				assignments: [],
				isTeacher: false,
				showModal: false,
				showStudentSubmissionsModal: false,
				showCreateAssignmentModal: false,
				modalAssignment: {},
				currentAssignment: {},
				uploadedFile: null,
				isPastDueDate: false,
			};
		},
		async mounted() {
			this.courseName = this.$route.params.name;
			await this.fetchAssignments();
			// 假设通过某种方式判断用户角色
			this.isTeacher = await this.checkUserRole();
		},
		watch: {
			'modalAssignment.dueDate': function(newDate) {
				if (newDate) {
					const dueDate = new Date(newDate);
					const now = new Date();
					this.isPastDueDate = now > dueDate; // 比较当前时间与截止时间
				}
			},
		},
		methods: {
			async fetchAssignments() {
				const assignments = await getAssignMent(this.$route.params.name);
        this.assignments = assignments.map(assignment => {
          if (assignment.attachment) {
            assignment.attachment = `http://localhost:8000${assignment.attachment}`;
          }
          assignment.submissions.map(sub=>{
            sub.file=`http://localhost:8000${sub.file}/${sub.studentId}`;
            return sub;
          });
          return assignment;
        });
			},
			viewAssignment(assignment) {
				this.modalAssignment = assignment;
				this.showModal = true;
			},
			viewStudentSubmissions(assignment) {
				this.currentAssignment = assignment;

        this.showStudentSubmissionsModal = true;
			},
			handleFileUpload(event) {
				this.uploadedFile = event.target.files[0];
			},
			async submitAssignment() {
				try {
					if (this.uploadedFile) {
						// 处理文件上传
						await uploadAssignMent(this.uploadedFile, this.$route.params.name, this.modalAssignment.id);
					}
					this.showModal = false;
					alert('上传成功');
				} catch (err) {
					console.error(err);
					alert('上传失败');
				}
			},
			async gradeAssignment(studentId, assignmentId, score) {
				if (score < 0 || score > 100 || score === undefined || score === null) {
					alert("请输入有效的分数（0-100）");
					return;
				}
				try {
					// 调用后端 API 提交分数
					await submitScore(studentId, assignmentId, this.courseName, score);
					alert("判分成功");
				} catch (error) {
					console.error(error);
					alert("判分失败，请重试");
				}
			},
			showCreateModal() {

				this.showCreateAssignmentModal = true;
				console.log(this.showCreateAssignmentModal)
			},
			async checkUserRole() {
				const res = await fetchUserInfo();

				if (res.role === 'teacher') return true;

				return false;
			},
		},
	};
</script>

<style scoped>


/* 页面标题 */
.page-title {
	font-size: 1.8em;
	color:#4caf50;
	text-align: center;
	margin-bottom: 20px;
}

/* 作业板块 */
.assignments-section {
	width: 90vw;
	padding: 20px;
	background-color: #e8f5e9;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 按钮样式 */
.create-btn,
.submit-btn,
.grade-btn {
	background-color: #4caf50;
	color: white;
	border: none;
	padding: 10px 15px;
	border-radius: 5px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.create-btn:hover,
.submit-btn:hover,
.grade-btn:hover {
	background-color: #388e3c;
}

/* 作业列表 */
.assignments-list {
	list-style: none;
	padding: 0;
}

.assignment-item {
	padding: 15px;
	border: 1px solid gray;
	border-radius: 5px;
	background-color: #fff;
	margin-bottom: 10px;
	cursor: pointer;
	transition: box-shadow 0.3s;
}

.assignment-item:hover {
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 标题和描述 */
.assignment-title {
	font-size: 1.2em;
	color: #4caf50;
	margin: 0;
}

.due-date {
	font-size: 0.9em;
	color: #757575;
	margin-left: 10px;
}

.assignment-description {
	margin: 10px 0;
	color: black;
}

/* 附件链接 */
.attachment-link a {
	color: #4caf50;
	text-decoration: none;
	font-weight: bold;
}

.attachment-link a:hover {
	text-decoration: underline;
}

/* 提交区域 */
.file-upload {
	display: block;
	margin-top: 15px;
}

.deadline-message {
	color: red;
	font-weight: bold;
}

/* 学生提交情况列表 */
.submissions-list {
	list-style: none;
	padding: 0;
}

.submission-item {
	padding: 10px;
	border-bottom: 1px solid gray;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.grading-section {
	display: flex;
	align-items: center;
}

.score-input {
	width: 60px;
	padding: 5px;
	border: 1px solid gray;
	border-radius: 3px;
	margin-right: 10px;
}
</style>
