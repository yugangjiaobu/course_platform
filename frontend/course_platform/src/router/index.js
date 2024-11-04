import LoginPage from '../components/LoginPage.vue';
import InfoPage from '../components/InfoPage.vue';
import HomePage from '../components/HomePage.vue';
import CourseListPage from '../components/CourseListPage.vue';
import CourseDetailsPage from '../components/CourseDetailsPage.vue';
import AssignmentPage from '../components/Assignment/AssignmentPage.vue';
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
	{
	    path: '/courseList',
	    name: 'CourseListPage',
	    component: CourseListPage,
	},
	{
	    path: '/coursedetail/:course',
	    name: 'CourseDetailsPage',
	    component: CourseDetailsPage,
	},
	{
	    path: '/course/:name/assignments',
	    name: 'AssignmentPage',
	    component: AssignmentPage,
	}
  // 其他路由...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
