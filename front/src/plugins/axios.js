import Vue from "vue";
import axios from "axios";
import Qs from 'qs';
import router from "@/router";
import store from "@/store"

// Full config:  https://github.com/axios/axios#request-config
axios.defaults.baseURL = process.env.VUE_APP_BASE_API || "",
axios.defaults.withCredentials = true;

axios.interceptors.request.use(
  config => {
    if (store.state.user.token) {
      config.headers['Authorization'] = store.state.user.token;
    }
    // params 转 QueryString
    if (config.params) {
      const url = config.url || '';
      const queryString = Qs.stringify(config.params, {arrayFormat: 'brackets'});
      config.params = {};
      config.url = `${url}${url.includes('?') && '&' || '?'}${queryString}`;
    }
    // post data Object 转为 FromData
    if (config.data && !(config.data instanceof FormData) && /application\/x-www-form-urlencoded/.test(config.headers['Content-Type'])) {
      const formData = new FormData()
      Object.entries(config.data).forEach(([key, value]) => {
        value !== undefined && value !== null && formData.append(key, String(value));
      })
      config.data = formData;
    }
    return config;
  },
  err => {
    // Do something with request error
    return Promise.reject(err);
  }
);

// Add a response interceptor
axios.interceptors.response.use(
  response => {
    if (response.data.data instanceof Object && response.data.data.code === 4001) {
      return router.push('/login')
    }
    if (response.data.data instanceof Object && response.data.data.code === 4003) {
      return router.push('/noPermission')
    }
    return response.data.data !== undefined ? response.data : response
  },
  error => {
    if (error.response.status === 401) {
      return router.push('/login')
    }
    if (error.response.status === 403) {
      return router.push('/noPermission')
    }
    return Promise.resolve({
      code: 500,
      msg: error.response.data && error.response.data.msg || error.response.statusText
    })
  }
);

const Plugin = {
  install: Vue => {
    Vue.$axios = axios;
  }
};
Plugin.install = Vue => {
  Vue.$axios = axios;
  window.axios = axios;
  Object.defineProperties(Vue.prototype, {
    $axios: {
      get() {
        return axios;
      }
    }
  });
};

Vue.use(Plugin);

export default axios;
