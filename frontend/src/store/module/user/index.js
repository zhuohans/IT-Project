import { get } from "@/util/cache"
import { TOKEN, USER_NAME, USER_ID } from "@/util/constants"

export default {
  namespaced: true,
  state: {
    token: get(TOKEN),
    userId: get(USER_ID),
    username: get(USER_NAME),
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setUserId(state, userId) {
      state.userId = userId;
    },
    setUsername(state, username) {
      state.username = username;
    },
  },
  actions: {
    acSetToken(context, token) {
      context.commit("setToken", token);
    },
    acSetUserId(context, userId) {
      context.commit("setUserId", userId);
    },
    acSetUsername(context, username) {
      context.commit("setUsername", username);
    },
    acClear({ commit }) {
      commit('setToken', null);
      commit('setUserId', null);
      commit('setUsername', null);
    }
  }
};