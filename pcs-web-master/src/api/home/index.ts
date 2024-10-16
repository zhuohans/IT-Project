import { post, get } from '@/utils/http'

const API = {
  getSpeciesList: '/pcs/system/species/list',
  viewSpecies: '/pcs/system/species/view',
  likeSpecies: '/pcs/system/species/like',
  collectSpecies: '/pcs/system/species/collect',
  commentSpecies: '/pcs/system/species/comment',
  delComment: '/pcs/system/species/comment/del',
  getClassificationList: '/pcs/system/species/classification/list',
  getSpeciesListByLatinName: '/pcs/system/species/listByLatinName',
  getLastSpecies: '/pcs/system/species/list/news',
  getSpeciesByLetter: '/pcs/system/species/list/byletter',
  deleteSpecies: '/pcs/system/species/del',
  updateSpecies: '/pcs/system/species/update',
}

export const viewSpecies = (id: string) => {
  return post(`${API.viewSpecies}/${id}`, null)
}
export const likeSpecies = (id: string, like: number) => {
  return post(`${API.likeSpecies}/${id}?like=${like}`, null)
}

export const collectSpecies = (id: string, collect: number) => {
  return post(`${API.collectSpecies}/${id}?collect=${collect}`, null)
}

export const commentSpecies = (id: string, comment: string) => {
  return post(`${API.commentSpecies}/${id}`, { comment })
}
export const delComment = (id: string, speciesId: string) => {
  return post(`${API.delComment}/${speciesId}/${id}`, null)
}

export const getClassificationList = (latinName: string) => {
  return post(`${API.getClassificationList}/${latinName}`, null)
}

export const getSpeciesListByLatinName = (latinName: string) => {
  return post(`${API.getSpeciesListByLatinName}/${latinName}`, null)
}

export const getLastSpecies = (params) => {
  return post(`${API.getLastSpecies}`, params)
}
export const deleteSpecies = (id) => {
  return post(`${API.deleteSpecies}/${id}`)
}
export const updateSpecies = (params) => {
  return post(`${API.updateSpecies}`, params)
}
