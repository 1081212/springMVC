import Vue from 'vue'


import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import "../src/assets/global.css"
import echarts from "echarts";
Vue.config.productionTip = false
Vue.use(ElementUI,{size:"small"});
import axios from 'axios'
Vue.prototype.$axios = axios
Vue.prototype.$echarts = echarts;
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
