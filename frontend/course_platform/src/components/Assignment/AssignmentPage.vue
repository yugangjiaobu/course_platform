<template>
	<div id="assignments-page">
		<h2>{{ isTeacher ? '教师端|作业' : '学生端|作业' }}</h2>

		<div v-if="isTeacher">
			<button @click="showCreateModal">新建作业</button>
			<ul>
				<li v-for="assignment in assignments" :key="assignment.id" @click="viewStudentSubmissions(assignment)">
					<h4>{{ assignment.title }} - {{ assignment.dueDate }}</h4>
					<p>{{ assignment.description }}</p>
					<div v-if="assignment.attachment">
						<a :href="assignment.attachment" download>下载附件</a>
					</div>
				</li>
			</ul>
		</div>

		<div v-else>
			<ul>
				<li v-for="assignment in assignments" :key="assignment.id" @click="viewAssignment(assignment)">
					{{ assignment.title }} - {{ assignment.dueDate }} - {{assignment.score}}
				</li>
			</ul>
		</div>

		<modal v-if="showModal" @close="showModal = false">
			<template #header>
				<h3>{{ modalAssignment.title }}</h3>
			</template>
			<template #body>
				<p>{{ modalAssignment.description }}</p>
				<div v-if="modalAssignment.attachment">
					<a :href="modalAssignment.attachment" download>下载附件</a>
				</div>
				<input v-if="!isPastDueDate" type="file" @change="handleFileUpload" />
			</template>
			<template #footer>
				<button v-if="!isPastDueDate" @click="submitAssignment">提交作业</button>
				<span v-else>作业已截至</span>
			</template>
		</modal>


		<modal v-if="showStudentSubmissionsModal" @close="showStudentSubmissionsModal = false">
			<template #header>
				<h3>{{ currentAssignment.title }} 学生提交情况</h3>
			</template>
			<template #body>
				<ul>
					<li v-for="submission in currentAssignment.submissions" :key="submission.studentId">
						{{ submission.studentName }} -
						<span v-if="submission.file">
							<a :href="submission.file" download>下载作业</a>
						</span>
						<span v-else>未提交</span>
						- score: {{ submission.score }}
						<div>
							<input type="number" v-model.number="submission.inputScore" min="0" max="100"
								placeholder="输入分数" style="width: 60px; margin-right: 10px;" />
							<button
								@click="gradeAssignment(submission.studentId, currentAssignment.id, submission.inputScore)">
								判分
							</button>
						</div>
					</li>
				</ul>

			</template>
		</modal>

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
				this.assignments = await getAssignMent(this.$route.params.name);
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
	#assignments-page {
		padding: 20px;
		font-family: 'Microsoft YaHei', sans-serif;
	}

	ul {
		list-style-type: none;
		padding: 0;
	}

	li {
		cursor: pointer;
		margin: 10px 0;
	}

	li:hover {
		text-decoration: underline;
	}
</style>