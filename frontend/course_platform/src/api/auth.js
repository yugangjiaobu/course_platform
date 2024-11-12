import axios from 'axios';
//axios.defaults.baseURL='http://154.201.66.144:8080';
axios.defaults.baseURL = 'http://localhost:8000';

function getToken() {
	return window.localStorage.getItem('token');
}

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

export const creatAssignMent = async (file,name,description,title) => {
	try {
		const token = getToken();
		// 创建 FormData 对象
		const formData = new FormData();
		formData.append('file', file); // 添加文件
		formData.append('course',name);//课程名称
		formData.append('description',description);//作业描述
		formData.append('title',title);//作业标题

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
