<template>
    <div id="home-page">
        <div id="glass"></div>
        <div class="options-container">
            <h2>首页</h2>
            <div class="option" @click="goTo('personalCenter')">
                <h3>个人中心</h3>
            </div>
            <div class="option" @click="goTo('myCourses')">
                <h3>我的课程</h3>
            </div>
            <div class="option" @click="goTo('notificationCenter')">
                <h3>通知中心</h3>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        methods: {
            async goTo(page) {
                try {
                    const token = await this.getToken(); // 获取token
                    window.localStorage.setItem('token', token); // 存储token

                    if (page === 'personalCenter') {
                        const userData = await this.fetchUserData(token); // 获取个人中心数据
                        console.log('User Data:', userData); // 处理个人中心数据（可根据需求进行修改）
                        this.$router.push('/info'); // 跳转到个人中心
                    } else if (page === 'myCourses') {
                        this.$router.push('/my-courses');
                    } else if (page === 'notificationCenter') {
                        this.$router.push('/notification-center');
                    }
                } catch (err) {
                    alert('操作失败');
                    console.error('Error:', err);
                }
            },
            async getToken() {
                // 模拟token获取，替换为实际的API调用
                return new Promise((resolve) => {
                    const token = 'your_generated_token_here'; // 替换为实际token
                    resolve(token);
                });
            },
            async fetchUserData(token) {
                try {
                    const response = await fetch('https://your-api-endpoint.com/userdata', {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }

                    const data = await response.json();
                    return data; // 返回获取的用户数据
                } catch (error) {
                    console.error('Failed to fetch user data:', error);
                    throw error; // 重新抛出错误以供调用者处理
                }
            }
        }
    };
</script>

<style scoped>
    @charset "UTF-8";

    #home-page {
        top: 0;
        left: 0;
        position: fixed;
        width: 100vw;
        height: 100vh;
        background: url('../assets/login.jpg'); /* 使用首页背景 */
        background-repeat: no-repeat;
        background-size: cover;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        color: green;
        font-family: 'Microsoft YaHei', 'Heiti SC', 'WenQuanYi Micro Hei', sans-serif;
    }

    h2 {
        margin-bottom: 20px;
    }

    #glass {
        z-index: 1;
        position: absolute;
        background-color: rgba(255, 255, 255, 0.5);
        height: 50vh;
        width: 50vw;
        backdrop-filter: blur(5px);
        border-radius: 2vw;
    }

    .options-container {
        z-index: 2;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .option {
        cursor: pointer;
        background-color: rgba(255, 255, 255, 0.8);
        border: 1px solid green;
        border-radius: 1vh;
        padding: 15px;
        margin: 10px;
        width: 30vw;
        text-align: center;
        transition: background-color 0.3s;
    }

        .option:hover {
            background-color: green;
            color: white;
        }
</style>
