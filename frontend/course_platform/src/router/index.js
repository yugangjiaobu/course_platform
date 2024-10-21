import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import InfoPage from '../components/InfoPage.vue';

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
  // 其他路由...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
