import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import HomeView from '@/views/Home'
import ForumView from '@/views/Forum'
import CalendarView from '@/views/Calendar'
import ProfileView from '@/views/Profile'
import LoginView from '@/views/Login'
import RegisterView from '@/views/Register'
import PlantDetailView from '@/views/Plants/Detail'
import CollectionsView from '@/views/Collections'
import ResetPasswordView from '@/views/ResetPassword'
import ProfileEditView from '@/views/Profile/Edit'
import UploadAvatarView from '@/views/Profile/UploadAvatar'
import AddForumView from '@/views/Forum/Add'
import PostDetailView from '@/views/Forum/Detail'
import EventsView from '@/views/Events'

const routes = [
  {
    path: '/',
    name: 'layout',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'index',
        name: 'index',
        component: HomeView
      },
      {
        path: 'forum',
        name: 'forum',
        component: ForumView
      },
      {
        path: 'forum/add',
        name: 'addForum',
        component: AddForumView
      },
      {
        path: 'posts/:id',
        name: 'postDetail',
        component: PostDetailView
      },
      {
        path: 'calendar',
        name: 'calendar',
        component: CalendarView
      },
      {
        path: 'events',
        name: 'events',
        component: EventsView
      },
      {
        path: 'profile',
        name: 'profile',
        component: ProfileView
      },
      {
        path: 'profile/edit',
        name: 'profileEdit',
        component: ProfileEditView
      },
      {
        path: 'avatar/upload',
        name: 'uploadAvatar',
        component: UploadAvatarView
      },
      {
        path: 'plants/:id',
        name: 'plantDetail',
        component: PlantDetailView
      },
      {
        path: 'collections',
        name: 'collections',
        component: CollectionsView
      },
      {
        path: 'resetPassword',
        name: 'resetPassword',
        component: ResetPasswordView
      },
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  console.log("router before..");
  next();
});

export default router
