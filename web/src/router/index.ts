import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/home-view.vue'
import AboutView from '../views/about-view.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminUser from '../views/admin/admin-user.vue'
import EditorView from '../views/editor-view.vue'

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
    component: AdminEbook
  },
  {
    path: '/admin/category',
    name: 'admin-category',
    component: AdminCategory
  },
  {
    path: '/admin/user',
    name: 'admin-user',
    component: AdminUser
  },
  {
    path: '/editor',
    name: 'editor',
    component: EditorView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
