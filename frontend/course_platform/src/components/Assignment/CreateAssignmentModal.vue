<template>
	<div class="modal">
		<div class="modal-content">
			<span class="close" @click="$emit('close')">&times;</span>
			<h3>新建作业</h3>
			<form @submit.prevent="createAssignment">
				<div class="form-group">
					<label for="title">作业标题</label>
					<input id="title" v-model="title" placeholder="请输入作业标题" required />
				</div>
				<div class="form-group">
					<label for="description">作业描述</label>
					<textarea id="description" v-model="description" placeholder="请输入作业描述" required></textarea>
				</div>
				<div class="form-group">
					<label for="attachment">附件</label>
					<input type="file" @change="handleFileUpload" />
				</div>
				<button type="submit" class="btn">创建作业</button>
			</form>
		</div>
	</div>
</template>

<script>
	import { creatAssignMent } from '../../api/auth.js';
	export default {
		data() {
			return {
				title: '',
				description: '',
				attachment: null,
			};
		},
		methods: {
			handleFileUpload(event) {
				this.attachment = event.target.files[0];
			},
			async createAssignment() {
				try {
					await creatAssignMent(this.attachment, this.$route.params.name, this.description, this.title);
					alert('发布成功');
					this.$emit('close');
				} catch (err) {
					console.error(err);
				}
			},
		},
	};
</script>

<style scoped>
	.modal {
		position: fixed;
		z-index: 1000;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		overflow: auto;
		background-color: rgba(0, 0, 0, 0.6);
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.modal-content {
		background-color: #fff;
		border-radius: 8px;
		box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
		padding: 20px;
		width: 90%;
		max-width: 500px;
		position: relative;
	}

	.close {
		color: #aaa;
		position: absolute;
		top: 10px;
		right: 15px;
		font-size: 24px;
		cursor: pointer;
	}

	.close:hover,
	.close:focus {
		color: black;
	}

	.form-group {
		margin-bottom: 15px;
	}

	label {
		display: block;
		margin-bottom: 5px;
		font-weight: bold;
	}

	input[type="text"],
	input[type="file"],
	textarea {
		width: 80%;
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 4px;
		font-size: 16px;
	}

	textarea {
		resize: vertical;
	}

	.btn {
		background-color: #4CAF50;
		color: white;
		border: none;
		padding: 10px 15px;
		border-radius: 4px;
		cursor: pointer;
		font-size: 16px;
		width: 100%;
	}

	.btn:hover {
		background-color: #45a049;
	}
</style>
