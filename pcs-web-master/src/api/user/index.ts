import { post, get, upload, syncPost } from '@/utils/http'

const API = {
  login: '/pcs/system/auth/user/login',
  register: '/pcs/system/auth/user/register',
  sendEmail: '/pcs/system/auth/user/send/mail',
  sendRegisterEmail: '/pcs/system/auth/user/send/register/mail',
  resetPassword: '/pcs/system/auth/user/reset/password',
  getUserInfo: '/pcs/system/auth/user/info',
  updateUserInfo: '/pcs/system/auth/user/update',
  changePassword: '/pcs/system/auth/user/change/password',
  uploadAvatar: '/pcs/system/species/uploadFile',
  getEventList: '/pcs/system/reminder/list/being',
  receiveCnt: '/pcs/system/userpage/receive/cnt',
  footprints: '/pcs/system/userpage/footprints/list',
  album: '/pcs/system/userpage/photos/list',
  add: '/pcs/system/species/add',
  getCollectList: '/pcs/system/userpage/collect/list',
  getGardenSpeciesList: '/pcs/system/userpage/garden/list',
  addToGarden: '/pcs/system/userpage/garden/add',
  updateToGarden: '/pcs/system/userpage/garden/update',
  deleteGardenSpecies: '/pcs/system/userpage/garden/delete',
  createReminder: '/pcs/system/reminder/create',
  getReminderList: '/pcs/system/reminder/list',
  deleteReminder: '/pcs/system/reminder/delete',
  updateReminder: '/pcs/system/reminder/being/update',
  updateReminderAll: '/pcs/system/reminder/being/updateAll',
}

export const login = (params: any) => {
  return post(API.login, params)
}
export const register = (params: any) => {
  return post(API.register, params)
}

export const sendEmail = (email) => {
  return get(`${API.sendEmail}?email=${email}`)
}

export const sendRegisterEmail = (email) => {
  return get(`${API.sendRegisterEmail}?email=${email}`)
}

export const resetPassword = (params) => {
  return post(API.resetPassword, params)
}

export const getUserInfo = () => {
  return get(`${API.getUserInfo}`)
}

export const updateUserInfo = (params: any) => {
  return post(API.updateUserInfo, params)
}

export const changePassword = (params: any) => {
  return post(API.changePassword, params)
}

export const uploadFile = (params) => {
  return upload(API.uploadAvatar, params)
}

export const receiveCnt = () => {
  return post(API.receiveCnt)
}
export const footprints = (page, type) => {
  return post(`${API.footprints}/${type}`, page)
}

export const album = (page) => {
  return post(`${API.album}`, page)
}
export const addSpecies = (params) => {
  return post(`${API.add}`, params)
}

export const getCollectList = (page) => {
  return post(`${API.getCollectList}`, page)
}

export const getGardenSpeciesList = () => {
  return post(`${API.getGardenSpeciesList}`)
}

export const addToGarden = (params) => {
  return post(`${API.addToGarden}`, params)
}

export const updateToGarden = (params) => {
  return post(`${API.updateToGarden}`, params)
}

export const deleteGardenSpecies = (params) => {
  return post(`${API.deleteGardenSpecies}`, params)
}

export const createReminder = (params) => {
  return post(`${API.createReminder}`, params)
}

export const deleteReminder = (id) => {
  return post(`${API.deleteReminder}/${id}`)
}

export const getReminderList = () => {
  return post(`${API.getReminderList}`)
}

export const getEventList = () => {
  return syncPost(`${API.getEventList}`)
}

export const updateReminder = (id) => {
  return post(`${API.updateReminder}/${id}`)
}

export const updateReminderAll = () => {
  return post(`${API.updateReminderAll}`)
}
