import { createRouter, createWebHistory } from 'vue-router'
import Summary from '../views/Summary.vue'
import LiveStream from '../views/LiveStream.vue'

const routes = [
  {
    path: '/',
    redirect: '/summary'
  },
  {
    path: '/summary',
    name: 'Summary',
    component: Summary
  },
  {
    path: '/live-stream',
    name: 'LiveStream',
    component: LiveStream
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
