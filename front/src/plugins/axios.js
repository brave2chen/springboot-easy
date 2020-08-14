import Vue from "vue";
import axios from "axios";
import Qs from 'qs';
import router from "@/router";
import store from "@/store"

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

const config = {
  baseURL: process.env.VUE_APP_BASE_API || "",
  // timeout: 60 * 1000, // Timeout
  withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

_axios.interceptors.request.use(
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
_axios.interceptors.response.use(
  response => response.data.data !== undefined ? response.data : response,
  error => {
    if (error.response.status === 401) {
      return router.push('/login')
    }
    return Promise.resolve({
      code: 500,
      msg: error.response.data && error.response.data.msg || error.response.statusText
    })
  }
);

const Plugin = {
  install: Vue => {
    Vue.$axios = _axios;
  }
};
Plugin.install = Vue => {
  Vue.$axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    $axios: {
      get() {
        return _axios;
      }
    }
  });
};

Vue.use(Plugin);

export default _axios;
