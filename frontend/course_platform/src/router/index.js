import LoginPage from '../components/LoginPage.vue';
import { createRouter, createWebHistory } from 'vue-router';

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
