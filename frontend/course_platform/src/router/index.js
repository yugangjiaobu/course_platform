import LoginPage from '../components/LoginPage.vue';
import InfoPage from '../components/InfoPage.vue';
import HomePage from '../components/HomePage.vue';
import { createRouter, createWebHistory } from 'vue-router';



const routes = [
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
    },
    {
        path: '/info',  
        name: 'InfoPage',        
        component: InfoPage,     
    },
    {
        path: '/home',
        name: 'HomePage',
        component: HomePage,
    },
  // 其他路由...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
