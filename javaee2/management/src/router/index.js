import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from "../layout/Layout.vue";
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:"/login",
    children:[
      {
        path:'/home',
        name:'Home',
        component:()=>import("../views/Home.vue")
      },
      {
        path:'write',
        name:'write',
        component:()=>import("../views/single.vue")
      },
      {
        path:'sign',
        name:'sign',
        component:()=>import("../views/dataPredict.vue")
      },
      {
        path:'/datas',
        name:'datas',
        component: ()=>import('../views/datas')
      },
      {
        path:'/center',
        name:'center',
        component: ()=>import('../views/center')
      },
      {
        path:'/model',
        name:'model',
        component: ()=>import('../views/model')
      }
    ]
  }, {
    path: '/Login',
    name: 'Login',
    component: () => import('../views/Login1')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/register')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
