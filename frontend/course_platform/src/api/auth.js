import axios from 'axios';
//axios.defaults.baseURL='http://154.201.66.144:8080';
axios.defaults.baseURL = 'http://localhost:8000';

function getToken() {
	return window.localStorage.getItem('token');
}
//登陆
export const loginRequest = async (userID, password) => {
	try {
		const response = await axios.post('/api/login', {
			userID,
			password,
		});
		console.log(response);
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('Login failed');
	}
};
//更新个人信息
export const updateInfo = async (email, tel) => {
	const token = getToken();
	try {
		const response = await axios.post('/api/infoupdate', {
			email,tel
		},{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('update failed');
	}
};
//获取课程列表
export const getCourseList = async () => {
	try {
		const token = getToken();
		const response = await axios.get('/api/courselist', {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		console.log(response.data);
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('GetCourseList failed');
	}
};
//检查是否登陆
export const checkLogin = async () => {
	try {
		const token = getToken();
		const response = await axios.get('/api/checklogin', {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('The user has not logged in');
	}
};
//获取个人信息
export const fetchUserInfo = async () => {
	try {
		const token = getToken();
		const response = await axios.get('/api/userinfo', {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('Get user info error');
	}
};
//获取课程信息
export const getCourseDetails = async (coursename) => {
	try {
		const token = getToken();
		const response = await axios.get('/api/coursedetails?name=' + coursename, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('Get course info error');
	}
};
//获取作业列表
export const getAssignMent = async (coursename) => {
	try {
		const token = getToken();
		const response = await axios.get('/api/assignments?course=' + coursename, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get assignment');
	}
};
//上传作业
export const uploadAssignMent = async (file,name,id) => {
	try {
		const token = getToken();
		// 创建 FormData 对象
		const formData = new FormData();
		formData.append('file', file); // 添加文件
		formData.append('course',name);//课程名称
		formData.append('id', id); // 添加作业 ID

		const response = await axios.post('/api/uploadassignment', formData, {
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'multipart/form-data', // 指定内容类型
			},
		});

		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error upload assignment');
	}
};
//创建作业
export const creatAssignMent = async (file,name,description,title,dueDate) => {
	try {
		const token = getToken();
		// 创建 FormData 对象
		const formData = new FormData();
		formData.append('file', file); // 添加文件
		formData.append('course',name);//课程名称
		formData.append('description',description);//作业描述
		formData.append('title',title);//作业标题
		formData.append('dueDate',dueDate);//作业截至日期

		const response = await axios.post('/api/creatassignment', formData, {
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'multipart/form-data', // 指定内容类型
			},
		});

		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error post assignment');
	}
};
//上传分数
export const submitScore = async (sid,aid,cname,score) => {
	try {
		const token = getToken();

		const response = await axios.post('/api/submitscore', {
			sid:sid,aid:aid,cname:cname,score:score
		}, {
			headers: {
				'Authorization': `Bearer ${token}`
			},
		});

		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error upload assignment');
	}
};
//发送通知
export const sendNotification = async (content) => {
	try {
		console.log(content);
		const token = getToken();
		const response = await axios.post('/api/notifications/send', {
			content:content
		}, {
			headers: {
				'Authorization': `Bearer ${token}`
			},
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error post notice');
	}
};
//获取通知
export const getNotifications = async () => {
	try {
		const token = getToken();
		const response = await axios.get('/api/notifications/view', {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		console.log(response.data);
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get notice');
	}
};
//获取资源
export const getResource = async (coursename) => {
	try {
		const token = getToken();
		const response = await axios.get('/api/getresource?coursename='+coursename, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get resourse');
	}
};
//删除资源
export const deleteResource = async (id) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/deleteresource',{
			id:id
		},{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error delete resource');
	}
};
//上传资源
export const uploadResource = async (coursename,file,name) => {
	try {
		const token = getToken();
		// 创建 FormData 对象
		const formData = new FormData();
		formData.append('file', file); // 添加文件
		formData.append('coursename',coursename);//课程名称
		formData.append('resourcename',name);//资源名称

		const response = await axios.post('/api/uploadresource', formData, {
			headers: {
				'Authorization': `Bearer ${token}`,
				'Content-Type': 'multipart/form-data', // 指定内容类型
			},
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error upload resource ');
	}
};
//获取帖子列表
export const getPostList = async (coursename) => {
	try {
		const token = getToken();
		const response = await axios.get('/api/getpostlist?coursename='+coursename, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get postlist');
	}
};
//获取帖子信息
export const getPost = async (coursename,postid) => {
	try {
		const token = getToken();
		const response = await axios.get(`/api/getpost?coursename=${coursename}&postid=${postid}`, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get post');
	}
};
//上传帖子
export const Post = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/post',formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error post');
	}
};
//点赞/取消点赞
export const LikeorUnlike = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/like',formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error like');
	}
};
//发表评论
export const Comment = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/comment',formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error comment');
	}
};
//删除帖子
export const DeletePost = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/deletepost',formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error delete post');
	}
};
//删除评论
export const DeleteComment = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post('/api/deletecomment',formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error delete comment');
	}
};
//获取点赞帖子列表
export const getLikePost = async () => {
	try {
		const token = getToken();
		const response = await axios.get(`/api/getlikelist`, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get likelist');
	}
};
//获取自己发布的帖子列表
export const getMyPost = async () => {
	try {
		const token = getToken();
		const response = await axios.get(`/api/getmylist`, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get mylist');
	}
};

//获取考试列表
export const getExam = async (coursename) => {
	try {
		const token = getToken();
		const response = await axios.get(`/api/getexams?coursename=${coursename}`, {
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		console.log(response.data);
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error get exams');
	}
};

//布置考试
export const setExam = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post(`/api/setexam`, formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error set exams');
	}
};

//删除考试
export const delExam = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post(`/api/deleteexam`, formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error del exam');
	}
};

//上传考试分数
export const uploadexamscore = async (formdata) => {
	try {
		const token = getToken();
		const response = await axios.post(`/api/uploadexamscore`, formdata,{
			headers: {
				Authorization: `Bearer ${token}`
			}
		});
		return response.data;
	} catch (error) {
		console.log(error.response);
		throw new Error('error upload score');
	}
};
