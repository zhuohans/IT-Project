import axios from 'axios'
import { showAlert, loading, hiddenLoading } from '@/utils/ComponentRegister'
import router from '@/router'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 60000,
})

let loadingApp: any

instance.interceptors.request.use(

  (config) => {


    if (config.url != '/pcs/system/reminder/list/being') {
      loadingApp = loading('loading...')
    }
    return config
  },
  (err) => {
    return Promise.reject(err)
  },
)

//配置响应拦截器
instance.interceptors.response.use(
  (response) => {
    const httpStatus = response.status
    if (httpStatus !== 200) {
      showAlert('服务不可用,请联系管理员', 'error')
    }
    let apiStatus = response.data.code
    hiddenLoading(loadingApp)
    switch (apiStatus) {
      case 200:
        return response.data.data
      case 401:
        console.log(response, 'fff')
        if (response.config.url != '/pcs/system/reminder/list/being') {
          showAlert('Please try again after logging in', 'error')
        }
        return Promise.reject(response.data)
      case 11011:
        showAlert('Your login has expired, please log in again', 'error')
        localStorage.removeItem('yzxToken')
        // router.push('/login')
        return Promise.reject(response.data)
      case 11012:
        showAlert('Your login has expired, please log in again', 'error')
        localStorage.removeItem('yzxToken')
        // router.push('/login')
        return Promise.reject(response.data)
      case 11016:
        showAlert('Your login has expired, please log in again', 'error')
        localStorage.removeItem('yzxToken')
        // router.push('/login')
        return Promise.reject(response.data)
      default:
        errorHandler(response.data)
        return Promise.reject(response.data)
    }
  },
  (err) => {
    hiddenLoading(loadingApp)
    return Promise.reject(err)
  },
)

const errorHandler = (data: any) => {
  showAlert(data.msg, 'error')
}

//封装请求的api
const api = (method = 'GET', url: string, data = {}) => {
  return instance({
    method,
    url,
    params: method === 'GET' ? data : {},
    data: method === 'POST' || method === 'PUT' ? data : {},
  })
}

export const get = (url: string, data: any) => api('GET', url, data)
export const post = (url: string, data: any) => api('POST', url, data)

export const put = (url: string, data: any) => api('PUT', url, data)
export const remove = (url: string, data: any) => api('DELETE', url, data)
export const upload = (url: string, data: any) =>
  instance({
    method: 'POST',
    url,
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
export const syncPost = async (url: string, data: any) =>
  instance({
    method: 'POST',
    url,
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
