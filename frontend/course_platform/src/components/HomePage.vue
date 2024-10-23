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
            goTo(page) {
                // 跳转部分已注释
                /*
                if (page === 'personalCenter') {
                  this.$router.push('/personal-center');
                } else if (page === 'myCourses') {
                  this.$router.push('/my-courses');
                } else if (page === 'notificationCenter') {
                  this.$router.push('/notification-center');
                }
                */
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
