<template>
	<div class="post-details-page" v-if="post">
		<div class="nav">
			<h2>{{ post.title }}</h2>
			<p class="author">作者: {{ post.author }} | 发布日期: {{ post.time }}</p>
		</div>
		<div class="show">
			<div v-if="post.imgs.length" class="imgcontainer">
				<img v-for="img in post.imgs" :src="baseurl+img" :alt="post.title" :key="img" />
			</div>
			<p class="content">{{ post.content }}</p>
		</div>
		<button class="like" @click="likePost">{{ post.ifLike ? '取消点赞' : '点赞' }} ({{ post.likes }})</button>

		<div class="comments">
		<div id="c"><h3>评论</h3><div class="input">
			<input v-model="newComment" placeholder="输入评论" />
			<button @click="submitComment">评论</button></div>
			
		</div>
			<ul>
				<li v-for="comment in post.commits" :key="comment.commentId">
					<p>{{ comment.name }}: {{ comment.content }}</p>
					<button v-if="comment.ifYours || isTeacher" @click="deleteComment(comment.commentId)">
						删除
					</button>
				</li>
			</ul>
		</div>

		
		<button class="back" @click="$router.push(`/course/${this.courseName}/discuss`)">返回</button>
	</div>
	<div v-else>
		<p>加载中...</p>
	</div>
</template>

<script>
	import {
		getPost,
		LikeorUnlike,
		Comment,
		DeleteComment,
		fetchUserInfo
	} from '../../api/auth.js';

	export default {
		data() {
			return {
        baseurl:'http://localhost:8000',
				courseName: this.$route.params.name,
				postid: this.$route.params.postid,
				post: null,
				newComment: '',
				isTeacher: false, // 替换为权限判断
			};
		},
		async mounted() {
			try {
				await this.checkUserRole();
				await this.loadPostDetails();
			} catch (err) {
				console.error(err);
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
			async loadPostDetails() {
				this.post = await getPost(this.courseName, this.postid);
			},
			async likePost() {
				const formdata = {
					coursename: this.courseName,
					postid: this.postid,
					iflike: !this.post.ifLike
				};
        //console.log(formdata);
				await LikeorUnlike(formdata);
				this.loadPostDetails();
				
			},
			async submitComment() {
				const formdata = {
					coursename: this.courseName,
					postid: this.postid,
					content: this.newComment
				};
				await Comment(formdata);
				this.newComment = '';
				this.loadPostDetails();
			},
			async deleteComment(commentid) {
				const formdata = {
					coursename: this.courseName,
					postid: this.postid,
					commentid: commentid
				};
				await DeleteComment(formdata);
				this.loadPostDetails();
			},
		},
	};
</script>

<style scoped>
	/* 添加样式 */
	*{
		margin: 0;
		padding: 0;
	}
	.post-details-page {
		height: 99vh;
		width: 99vw;
		overflow-y: auto;
		overflow-x: hidden;
	}

	.imgcontainer {
		max-width: 34vw;
		max-height: 34vw;
		min-width: 10vw;
		min-height: 10vw;
		display: flex;
		flex-wrap: wrap;
		gap: 2vw;
	}

	img {
		width: 10vw;
		height: 10vw;
		object-fit: cover;
	}
	
	.nav{
		width: 100%;
		height: 100px;
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: green;
		color: white;
		margin-bottom: 20px;
	}
	h2,h3{
		font-size: 30px;
		font-weight: normal;
		margin-left: 40px;
	}
	.author{
		margin-right: 50px;
		font-size: 15px;
	}
	.content{
		border: 0.5px solid grey;
		width: 50%;
		min-height: 20%;
		max-height: 50%;
		overflow-y: auto;
		margin-left: 5vw;
		padding: 10px;
	}
	.show{
		display: flex;
		justify-content: center;
		margin-bottom: 20px;
	}
	.like{
		position: fixed;
		right: 20px;
		bottom: 20px;
		width: 80px;
		height: 40px;
		background-color: white;
		border-radius: 5px;
		border: 1px solid green;
		color: green;
		cursor: pointer;
	}
	
	.comments{
		width: 100%;
		margin-bottom: 10vh;
	}
	.comments ul{
		width: 100%;
		margin-top: 20px;
		margin-left:50px;
		list-style: none;
		display: flex;
		flex-direction: column;
	}
	.comments li{
		width: 50%;
		height: 40px;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 0.5px solid grey;
	}
	.comments button{
		width: 50px;
		height: 30px;
		background-color: darkred;
		color: white;
		border-radius: 5px;
	}
	.back{
		position: fixed;
		bottom: 70px;
		right: 20px;
		width: 80px;
		height: 40px;
		background-color: white;
		border-radius: 5px;
		border: 1px solid red;
		color: red;
		cursor: pointer;
	}
	#c{
		display: flex;
	}
	.input{
		width: 300px;
		height: 40px;
		display: flex;
		justify-content: space-around;
		align-items: center;
	}
	.input input{
		width: 200px;
		height: 40px;
		outline: none;
		box-sizing: border-box;
		padding-left: 10px;
		border: none;
		border: 0.5px solid grey;
		border-radius: 5px;
	}
	.input button{
		width: 80px;
		height: 40px;
		background-color: green;
		border-radius: 5px;
		border: none;
		color: white;
	}
</style>
