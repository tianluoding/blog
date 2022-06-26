import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import axios from 'axios';
import { Tool } from './util/tool'

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数: ', config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(response => {
    // Do something before response is sent
    console.log('返回结果: ', response);
    return response;
}, error => {
    // Do something with response error
    return Promise.reject(error);
});

createApp(App).use(store).use(router).use(Antd).mount('#app')
