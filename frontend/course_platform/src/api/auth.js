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
