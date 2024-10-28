import { createRouter, createWebHistory } from 'vue-router'
import { album } from '@/api/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'overview',
      component: () => import('@/pages/index.vue'),
      redirect: '/home',
      children: [
        {
          path: '/home',
          name: 'home',
          component: () => import('@/pages/home/index.vue'),
          children: [
            {
              path: '/home/speciesDetail',
              name: 'homeSpeciesDetail',
              component: () => import('@/components/species_detail/index.vue'),
            },
          ],
        },
        {
          path: '/user',
          name: 'user',
          component: () => import('@/pages/user/index.vue'),
          redirect: '/user/footprints',
          children: [
            {
              path: '/user/footprints',
              name: 'footprints',
              component: () => import('@/pages/user/footprints/index.vue'),
              children: [
                {
                  path: '/user/footprints/speciesDetail',
                  name: 'footprintsSpeciesDetail',
                  component: () =>
                    import('@/components/species_detail/index.vue'),
                },
              ],
            },
            {
              path: '/user/album',
              name: 'album',
              component: () => import('@/pages/user/album/index.vue'),
              children: [
                {
                  path: '/user/album/speciesDetail',
                  name: 'albumSpeciesDetail',
                  component: () =>
                    import('@/components/species_detail/index.vue'),
                },
              ],
            },
            {
              path: '/user/garden',
              name: 'garden',
              component: () => import('@/pages/user/garden/index.vue'),
            },
          ],
        },
        {
          path: '/user/calendar',
          name: 'calendar',
          component: () => import('@/pages/user/calendar/index.vue'),
        },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/components/login/index.vue'),
    },
    {
      path: '/forgotPassword',
      name: 'forgotPassword',
      component: () => import('@/components/forgot_password/index.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/components/register/index.vue'),
    },
  ],
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('yzxToken')
  // if (to.path == '/forgotPassword') {
  //   next()
  // }
  // if (to.path != '/login' && !token) {
  //   next('/login')
  // }
  next()
})

export default router
