export default {
  namespaced: true,
  state: {
    message: '',
  },
  mutations: {
    setError(state, message) {  
      state.errorMessage = message;  
    },  
    clearError(state) {  
      state.errorMessage = '';  
    } 
  },
  actions: {
    acSetError(context, message) {
      context.commit("setError", message);
    },
    acClearError(context) {
      context.commit("clearError");
    }
  }
};