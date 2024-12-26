<template>
	<div class="exam-page">
		<h1>{{ coursename }} 考试模块</h1>
		<button @click="$router.back()" style="margin-bottom: 20px;">返回</button>
		<!-- 教师端功能 -->
		<div v-if="isTeacher">
			<button @click="showExamModal = true" style="margin-right: 20px;">创建考试</button>
			<button @click="showTestModal = true">创建测验</button>

			<h2>测验列表</h2>
			<ul>
				<li v-for="test in tests" :key="test.testname">
					{{ test.testname }}
					<button @click="viewTestResults(test)">查看成绩</button>
				</li>
			</ul>

			<h2>考试列表</h2>
			<ul>
				<li v-for="exam in exams" :key="exam.examid">
					<strong>考试名称：</strong>{{ exam.examName }}<br>
					<strong>考试地点：</strong>{{ exam.location }}<br>
					<strong>考试时间：</strong>{{ exam.time }}<br>
					<strong>注意事项：</strong>{{ exam.notice }}<br>
					<strong>考试编号：</strong>{{ exam.examId }}<br>
				</li>
			</ul>
		</div>

		<!-- 学生端功能 -->
		<div v-else>
			<h2>测验列表</h2>
			<ul>
				<li v-for="test in tests" :key="test.testname">
					{{ test.testname }} - 截止时间: {{ test.deadline }} - 分数: {{ test.score }}
					<template v-if="test.score !== null">
						<p>已作答</p>
					</template>
					<template v-else-if="new Date() > new Date(test.deadline)">
						<p>已经过期</p>
					</template>
					<template v-else>
						<button @click="viewTestDetail(test.testname)">进入测验</button>
					</template>
				</li>
			</ul>


			<h2>考试列表</h2>
			<ul>
				<li v-for="exam in exams" :key="exam.examid">
					<strong>考试名称：</strong>{{ exam.examName }}<br>
					<strong>考试地点：</strong>{{ exam.location }}<br>
					<strong>考试时间：</strong>{{ exam.time }}<br>
					<strong>注意事项：</strong>{{ exam.notice }}<br>
					<strong>试卷下载：</strong><a :href="exam.url" download>下载</a><br>
					<strong>座位编号：</strong>{{ exam.seatId }}<br>
				</li>
			</ul>
		</div>

		<!-- 创建考试模态框 -->
		<Modal :visible="showExamModal" @close="showExamModal = false">
			<h3>创建考试</h3>
			<form @submit.prevent="createExam">
				<input type="text" v-model="examForm.examname" placeholder="考试名称" required />
				<input type="text" v-model="examForm.location" placeholder="考试地点" required />
				<input type="datetime-local" v-model="examForm.time" required />
				<input type="file" @change="handleExamFile" required />
				<textarea v-model="examForm.notice" placeholder="考试注意事项"></textarea>
				<div>
					座位号排序:
					<label>
						<input type="radio" v-model="examForm.setstate" :value="0" />
						顺序
					</label>
					<label>
						<input type="radio" v-model="examForm.setstate" :value="1" />
						随机
					</label>
				</div>
				<button type="submit">提交</button>
			</form>
		</Modal>

		<!-- 创建测验模态框 -->
		<Modal :visible="showTestModal" @close="showTestModal = false">
			<h3>创建测验</h3>
			<form @submit.prevent="createTest">
				<!-- 测验名称 -->
				<input type="text" v-model="testForm.testname" placeholder="测验名称" required />

				<!-- 截止时间 -->
				<input type="datetime-local" v-model="testForm.deadline" required />

				<!-- 选择题 -->
				<div v-for="(choice, index) in testForm.choices" :key="index">
					<h4>题目 {{ index + 1 }}</h4>
					<input v-model="choice.title" type="text" placeholder="题目描述" required />

					<div v-for="(option, optionIndex) in choice.options" :key="optionIndex">
						<input v-model="choice.options[optionIndex]" type="text" :placeholder="`选项 ${optionIndex + 1}`"
							required />
					</div>

					<!-- 正确答案 -->
					<select v-model="choice.right">
						<option :value="0">选项1</option>
						<option :value="1">选项2</option>
						<option :value="2">选项3</option>
						<option :value="3">选项4</option>
					</select>

					<button type="button" @click="removeChoice(index)" style="margin-bottom: 20px;">删除此题目</button>
				</div>

				<!-- 添加新题目按钮 -->
				<button type="button" @click="addChoice">添加选择题</button>

				<button type="submit">提交</button>
			</form>
		</Modal>

		<!-- 查看测验成绩模态框 -->
		<Modal :visible="showResultsModal" @close="showResultsModal = false">
			<h3>测验成绩</h3>
			<table>
				<thead>
					<tr>
						<th>学生姓名</th>
						<th>成绩</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="student in currentTestResults" :key="student.name">
						<td>{{ student.name }}</td>
						<td>{{ student.score }}</td>
					</tr>
				</tbody>
			</table>
		</Modal>

		<!-- 测验详情模态框 -->
		<Modal :visible="showTestDetailModal" @close="showTestDetailModal = false">
			<h3>测验详情</h3>
			<form v-if="Array.isArray(currentTestDetail) && currentTestDetail.length > 0" @submit.prevent="submitTest">
				<div v-for="(question, index) in currentTestDetail" :key="question.id">
					<h4>{{ question.title }}</h4>
					<div v-for="(option, optionIndex) in question.options" :key="optionIndex">
						<input type="radio" :id="`option-${index}-${optionIndex}`" :value="optionIndex"
							v-model="answers[index]" required />
						<label :for="`option-${index}-${optionIndex}`">{{ option }}</label>
					</div>
				</div>
				<button type="submit">提交</button>
			</form>
		</Modal>

	</div>
</template>


<script>
	import axios from "axios";
	import {
		getExam,
		getTest,
		getTestDetail,
		setExam,
		setTest,
		uploadTest,
		fetchUserInfo
	} from "../../api/auth.js";
	import Modal from "./Modal.vue";

	export default {
		components: {
			Modal
		},
		data() {
			return {
				coursename: this.$route.params.name,
				isTeacher: false, // 根据用户身份设置
				exams: [],
				tests: [],
				showExamModal: false,
				showTestModal: false,
				showResultsModal: false,
				showTestDetailModal: false,
				currenttestname: '',
				examForm: {
					coursename: '',
					examname: "",
					location: "",
					setstate: 0,
					time: "",
					notice: "",
					file: null,
				},
				testForm: {
					testname: "",
					deadline: "",
					choices: [{
						title: "",
						options: ["", "", "", ""],
						right: 0,
					}, ],
				},
				currentTestResults: [],
				currentTestDetail: [],
				answers: [],
				currentQuestionIndex: 0,
			};
		},
		computed: {
			isLastQuestion() {
				return (
					this.currentTestDetail &&
					Array.isArray(this.currentTestDetail) &&
					this.currentQuestionIndex === this.currentTestDetail.length - 1
				);
			},
			hasNextQuestion() {
				return (
					this.currentTestDetail &&
					Array.isArray(this.currentTestDetail) &&
					this.currentQuestionIndex < this.currentTestDetail.length - 1
				);
			},
		},
		methods: {
			// 添加新的选择题
			addChoice() {
				this.testForm.choices.push({
					title: "",
					options: ["", "", "", ""],
					right: 0,
				});
			},
			// 删除现有的选择题
			removeChoice(index) {
				this.testForm.choices.splice(index, 1);
			},
			nextQuestion() {
				if (this.currentQuestionIndex < this.currentTestDetail.length - 1) {
					this.currentQuestionIndex++;
				}
			},
			async fetchExamsAndTests() {
				try {
					let result = await getExam(this.coursename);
					this.exams = result.map((exam) => {
						if (exam.url) {
							exam.url = `http://localhost:8000${exam.url}`;
						}
						return exam;
					});
					this.tests = await getTest(this.coursename);
				} catch (error) {
					console.error("Failed to fetch exams or tests", error);
				}
			},
			handleExamFile(event) {
				this.examForm.file = event.target.files[0];
			},
			async createExam() {
				const formData = new FormData();
				Object.keys(this.examForm).forEach(key => {
					//console.log(this.examForm[key]);
					formData.append(key, this.examForm[key]);
				});
				try {
					await setExam(formData);
					this.fetchExamsAndTests();
					this.showExamModal = false;
				} catch (error) {
					console.error("Failed to create exam", error);
				}
			},
			async createTest() {
				try {
					const testData = {
						coursename: this.coursename,
						testname: this.testForm.testname,
						deadline: this.testForm.deadline,
						choices: this.testForm.choices.map(choice => ({
							title: choice.title,
							options: choice.options,
							right: choice.right,
						})),
					};

					await setTest(testData); // 将测验数据传递给后端
					this.fetchExamsAndTests(); // 重新获取考试和测验列表
					this.showTestModal = false; // 关闭模态框
				} catch (error) {
					console.error("Failed to create test", error);
				}
			},
			viewTestResults(test) {
				this.currentTestResults = test.studentScores; // 直接从测试数据中提取学生成绩
				this.showResultsModal = true; // 显示成绩详情弹窗
			},
			async viewTestDetail(testname) {
				try {
					this.currentTestDetail = await getTestDetail(this.coursename, testname);
					this.currenttestname = testname;
					this.showTestDetailModal = true;
				} catch (error) {
					console.error("Failed to fetch test detail", error);
				}
			},
			async submitTest() {
				try {
					const payload = {
						coursename: this.coursename,
						testname: this.currenttestname, // 确保有测试名称
						answers: this.currentTestDetail.map((question, index) => ({
							id: question.id, // 使用题目 ID
							answer: this.answers[index], // 用户选择的答案索引
						})),
					};

					await uploadTest(payload); // 将答案提交给后端
					this.showTestDetailModal = false; // 隐藏模态框
				} catch (error) {
					console.error("Failed to submit test", error);
				}
			},
		},
		async created() {
			this.fetchExamsAndTests();
			const res = await fetchUserInfo();
			if (res.role === 'teacher') {
				this.isTeacher = true;
			}
			this.examForm.coursename = this.coursename;
		},
	};
</script>

<style scoped>
	.exam-page {
		font-family: Arial, sans-serif;
		padding: 20px;
		background-color: #f7f8f3;
		/* 浅绿色背景 */
		color: #333;
		width: 100vw;
		height: 100vh;
		overflow-y: auto;
	}

	h1 {
		color: #5c8c57;
		/* 浅绿色主题 */
		text-align: center;
		margin-bottom: 20px;
	}

	button {
		padding: 10px 15px;
		background-color: #5c8c57;
		color: white;
		border: none;
		border-radius: 5px;
		cursor: pointer;
		font-size: 16px;
		transition: background-color 0.3s;
	}

	button:hover {
		background-color: #4c7d4a;
		/* 鼠标悬停效果 */
	}

	h2 {
		color: #5c8c57;
		margin-top: 20px;
		font-size: 20px;
	}

	ul {
		padding-left: 20px;
	}

	li {
		margin-bottom: 10px;
	}

	strong {
		font-weight: bold;
	}

	a {
		color: #5c8c57;
	}

	a:hover {
		text-decoration: underline;
	}

	input,
	select,
	textarea {
		width: 100%;
		padding: 10px;
		margin: 10px 0;
		border: 1px solid #ccc;
		border-radius: 5px;
		font-size: 16px;
	}

	textarea {
		resize: vertical;
	}

	button[type="submit"] {
		margin-top: 20px;
		width: 100%;
	}

	form {
		display: flex;
		flex-direction: column;
	}

	form button[type="submit"] {
		background-color: #5c8c57;
		color: white;
		border-radius: 5px;
		padding: 12px;
		font-size: 16px;
		border: none;
		cursor: pointer;
		transition: background-color 0.3s;
	}

	form button[type="submit"]:hover {
		background-color: #4c7d4a;
	}

	@media (max-width: 768px) {
		.modal-content {
			width: 90vw;
			/* 在小屏幕上占宽度的90% */
			padding: 20px;
		}

		button {
			font-size: 14px;
			padding: 8px 12px;
		}

		input,
		select,
		textarea {
			font-size: 14px;
			padding: 8px;
		}
	}
</style>
