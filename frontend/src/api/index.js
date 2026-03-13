import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
})

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 如果响应数据中有 data 字段，直接返回响应对象
    return response.data
  },
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// 获取记忆总结列表
export const getMemorySummaries = (params = {}) => {
  return api.get('/memory-summary', { params })
}

// 获取单个记忆总结
export const getMemorySummary = (id) => {
  return api.get(`/memory-summary/${id}`)
}

// 创建记忆总结
export const createMemorySummary = (data) => {
  return api.post('/memory-summary', data)
}

// 更新记忆总结
export const updateMemorySummary = (id, data) => {
  return api.put(`/memory-summary/${id}`, data)
}

// 删除记忆总结
export const deleteMemorySummary = (id) => {
  return api.delete(`/memory-summary/${id}`)
}

export default api
