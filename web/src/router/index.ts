import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/home-view.vue'
import AboutView from '../views/about-view.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminUser from '../views/admin/admin-user.vue'
import EditorView from '../views/editor-view.vue'
import ContentView from '../views/content-view.vue'
import { Tool } from '@/util/tool'
import store from '../store'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin/ebook',
    name: 'admin-ebook',
    component: AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'admin-category',
    component: AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/user',
    name: 'admin-user',
    component: AdminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/editor/:id',
    name: 'editor',
    component: EditorView
  },
  {
    path: '/content/:id',
    name: 'content',
    component: ContentView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
})

export default router
