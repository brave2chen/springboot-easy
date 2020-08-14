import $axios from '@/plugins/axios'

const state = {
  user: null,
  token: '',
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, user) => {
    state.user = user
  },
}


const actions = {
  // user logout
  async login({commit}, userInfo) {
    const response = await $axios.post('/login', userInfo, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });
    if (response.data) {
      commit('SET_TOKEN', "Basic " + response.data.token)
      commit('SET_USER', response.data.user)
    }
    return response;
  },
  // user logout
  async logout({commit}) {
    commit('SET_TOKEN', '')
    commit('SET_USER', null)
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
