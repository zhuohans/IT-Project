import axios from "axios";
import { TOKEN } from "./constants";
import { get } from "./cache"
import store from "../store";
import router from '../router'

const instance = axios.create({
  baseURL: "http://localhost:5000/api",
  timeout: 5000
});


/**
 * The request interceptor
 */
instance.interceptors.request.use(config => {
  // get token from local storage
  const localToken = get(TOKEN);
  // get token from vuex
  // console.log("state: ", store.state.user);
  const vuexToken = store.state.user.token;
  const token = localToken || vuexToken;
  // Add the token to the request header if it exists
  // console.log("token: ", token);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  if (error.response && error.response.data && error.response.data.message) {  
    store.commit('notification/setError', error.response.data.message);  
  } else {  
    store.commit('notification/setError', 'An unknown error occurred');  
  }
  return Promise.reject(error);
});

instance.interceptors.response.use(
  (response) => {
    // Handle successful responses
    return response;
  },
  (error) => {
    if (error.response && error.response.data && error.response.data.message) {  
      store.commit('notification/setError', error.response.data.message);  
    } else {  
      store.commit('notification/setError', 'An unknown error occurred');  
    }
    console.log(error);
    if (error.status === 401) {
      router.push("/login");
    }
    return Promise.reject(error);
  }
);

export default instance;
