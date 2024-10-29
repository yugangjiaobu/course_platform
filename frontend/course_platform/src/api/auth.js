import axios from 'axios';
//axios.defaults.baseURL='http://154.201.66.144:8080';
axios.defaults.baseURL='http://localhost:3000';

function getToken(){
	return window.localStorage.getItem('token');
}

export const loginRequest = async (userID, password) => {
  try {
    const response = await axios.post('/api/login', {
      userID,
      password,
    });
    return response.data;
  } catch (error) {
    throw new Error('Login failed');
  }
};
