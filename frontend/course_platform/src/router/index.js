import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';

const routes = [
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
  },
  // 其他路由...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
