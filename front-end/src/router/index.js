import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'login',
    component: () => import("../views/loginView.vue")
  },
  {
    path: '/report',
    name: 'report',
    component: () => import("../views/reportView.vue"),
    meta: { 
      requiresAuth: true,
      title: 'Relatório de Recursos Reservados'
     }
  },
  {
    path: '/user/userManagement',
    name: 'User Management',
    component: () => import("../components/userManagement.vue"),
    meta: { 
      requiresAuth: true,
      title: 'Gerenciamento de Usuários'
     }
  },
  {
    path: '/resource/resourceManagement',
    name: 'Resource Management',
    component: () => import("../components/resourceManagement.vue"),
    meta: { 
      requiresAuth: true,
      title: 'Gerenciamento de Recursos'
     }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = sessionStorage.getItem('isAuthenticated');

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next({ name: 'login' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
