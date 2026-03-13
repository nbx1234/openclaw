<template>
  <div class="summary-page">
    <header class="page-header">
      <div class="container">
        <h1>记忆系统</h1>
        <p class="subtitle">查看和管理你的记忆总结</p>
      </div>
    </header>

    <main class="page-content">
      <div class="container">
        <!-- 总结卡片区域 -->
        <section class="summary-section">
          <h2 class="section-title">最新总结</h2>
          <div class="summary-cards">
            <div v-if="loading" class="loading-state">
              <div class="spinner"></div>
              <p>加载中...</p>
            </div>
            <div v-else-if="summaries.length === 0" class="empty-state">
              <p>暂无记忆总结</p>
            </div>
            <div v-else class="cards-grid">
              <div v-for="summary in summaries.slice(0, 3)" :key="summary.id" class="card summary-card">
                <div class="card-header">
                  <h3>{{ summary.title }}</h3>
                  <span class="date-badge">{{ formatDateShort(summary.createTime) }}</span>
                </div>
                <div class="card-content markdown-content" v-html="renderMarkdown(summary.content)"></div>
                <div class="card-footer">
                  <button class="btn btn-secondary btn-sm" @click="viewDetail(summary)">查看详情</button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 历史记录表格 -->
        <section class="history-section">
          <h2 class="section-title">历史记录</h2>
          <div class="card">
            <div v-if="loading" class="loading-state">
              <div class="spinner"></div>
              <p>加载中...</p>
            </div>
            <div v-else-if="summaries.length === 0" class="empty-state">
              <p>暂无历史记录</p>
            </div>
            <table v-else class="table">
              <thead>
                <tr>
                  <th>日期</th>
                  <th>内容</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="summary in summaries" :key="summary.id">
                  <td>{{ formatDateShort(summary.createTime) }}</td>
                  <td class="content-cell">
                    <span class="content-preview">{{ truncateContent(summary.content) }}</span>
                  </td>
                  <td>{{ formatDateTime(summary.createTime) }}</td>
                  <td class="action-cell">
                    <button class="btn btn-secondary btn-sm" @click="viewDetail(summary)">查看</button>
                    <button class="btn btn-danger btn-sm" @click="deleteItem(summary.id)">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </main>

    <!-- 详情模态框 -->
    <div v-if="selectedSummary" class="modal-overlay" @click="selectedSummary = null">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h2>{{ selectedSummary.title }}</h2>
          <button class="modal-close" @click="selectedSummary = null">×</button>
        </div>
        <div class="modal-body markdown-content" v-html="renderMarkdown(selectedSummary.content)"></div>
        <div class="modal-footer">
          <p class="modal-date">创建时间：{{ formatDateTime(selectedSummary.createTime) }}</p>
          <button class="btn btn-secondary" @click="selectedSummary = null">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMemorySummaries, deleteMemorySummary } from '../api/index.js'
import { renderMarkdown } from '../utils/markdown.js'
import { formatDate, formatDateShort, formatDateTime } from '../utils/date.js'

const summaries = ref([])
const loading = ref(false)
const selectedSummary = ref(null)

const fetchSummaries = async () => {
  loading.value = true
  try {
    const response = await getMemorySummaries()
    summaries.value = response.data || []
  } catch (error) {
    console.error('Failed to fetch summaries:', error)
    summaries.value = []
  } finally {
    loading.value = false
  }
}

const truncateContent = (content) => {
  if (!content) return ''
  const text = content.replace(/[#*`\[\]()]/g, '').trim()
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

const viewDetail = (summary) => {
  selectedSummary.value = summary
}

const deleteItem = async (id) => {
  if (!confirm('确定要删除这条记录吗？')) return
  try {
    await deleteMemorySummary(id)
    summaries.value = summaries.value.filter(s => s.id !== id)
  } catch (error) {
    console.error('Failed to delete summary:', error)
    alert('删除失败')
  }
}

onMounted(() => {
  fetchSummaries()
})
</script>

<style scoped lang="scss">
.summary-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.page-header {
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
  color: white;
  padding: 32px 16px;
  margin-bottom: 32px;

  h1 {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 8px;
  }

  .subtitle {
    font-size: 16px;
    opacity: 0.9;
  }
}

.page-content {
  padding-bottom: 32px;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #2c3e50;
}

.summary-section {
  margin-bottom: 32px;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 24px;
}

.summary-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16px;
    gap: 16px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;
      flex: 1;
    }

    .date-badge {
      background-color: #f8f9fa;
      color: #7f8c8d;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 12px;
      white-space: nowrap;
    }
  }

  .card-content {
    flex: 1;
    margin-bottom: 16px;
    overflow: hidden;
    max-height: 200px;
  }

  .card-footer {
    display: flex;
    gap: 8px;
  }
}

.markdown-content {
  line-height: 1.6;
  color: #2c3e50;

  h1, h2, h3, h4, h5, h6 {
    margin: 16px 0 8px 0;
    font-weight: 600;
  }

  p {
    margin-bottom: 16px;
  }

  ul, ol {
    margin-left: 24px;
    margin-bottom: 16px;
  }

  li {
    margin-bottom: 8px;
  }

  code {
    background-color: #f8f9fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
    font-size: 13px;
  }

  pre {
    background-color: #2c3e50;
    color: white;
    padding: 16px;
    border-radius: 8px;
    overflow-x: auto;
    margin-bottom: 16px;

    code {
      background-color: transparent;
      color: inherit;
      padding: 0;
    }
  }

  blockquote {
    border-left: 4px solid #3498db;
    padding-left: 16px;
    margin-left: 0;
    margin-bottom: 16px;
    color: #7f8c8d;
  }

  a {
    color: #3498db;
    text-decoration: underline;

    &:hover {
      color: #2980b9;
    }
  }
}

.history-section {
  margin-bottom: 32px;

  .card {
    background-color: #ffffff;
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }

  .content-cell {
    max-width: 300px;

    .content-preview {
      display: block;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      color: #7f8c8d;
    }
  }

  .action-cell {
    display: flex;
    gap: 8px;
  }
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 32px;
  color: #7f8c8d;

  p {
    margin-top: 16px;
  }
}

.table {
  width: 100%;
  border-collapse: collapse;

  thead {
    background-color: #f8f9fa;
    border-bottom: 2px solid #ecf0f1;
  }

  th {
    padding: 16px;
    text-align: left;
    font-weight: 600;
    color: #2c3e50;
  }

  td {
    padding: 16px;
    border-bottom: 1px solid #ecf0f1;
  }

  tbody tr {
    transition: background-color 0.2s ease;

    &:hover {
      background-color: #f8f9fa;
    }
  }
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: none;
  cursor: pointer;

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  &-primary {
    background-color: #3498db;
    color: white;

    &:hover:not(:disabled) {
      background-color: #2980b9;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
  }

  &-secondary {
    background-color: #ecf0f1;
    color: #2c3e50;

    &:hover:not(:disabled) {
      background-color: #d5dbdb;
    }
  }

  &-danger {
    background-color: #e74c3c;
    color: white;

    &:hover:not(:disabled) {
      background-color: #c0392b;
    }
  }

  &-sm {
    padding: 4px 8px;
    font-size: 12px;
  }

  &-lg {
    padding: 16px 24px;
    font-size: 16px;
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal {
  background-color: #ffffff;
  border-radius: 12px;
  max-width: 800px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #ecf0f1;

  h2 {
    font-size: 20px;
    font-weight: 600;
    margin: 0;
  }

  .modal-close {
    background: none;
    border: none;
    font-size: 28px;
    color: #7f8c8d;
    cursor: pointer;
    padding: 0;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: color 0.2s ease;

    &:hover {
      color: #2c3e50;
    }
  }
}

.modal-body {
  padding: 24px;
}

.modal-footer {
  padding: 24px;
  border-top: 1px solid #ecf0f1;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .modal-date {
    color: #7f8c8d;
    font-size: 14px;
    margin: 0;
  }
}

@media (max-width: 768px) {
  .page-header {
    h1 {
      font-size: 24px;
    }
  }

  .cards-grid {
    grid-template-columns: 1fr;
  }

  .modal {
    width: 95%;
    max-height: 90vh;
  }

  .history-section {
    .table {
      font-size: 12px;

      th, td {
        padding: 8px;
      }
    }
  }
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid #ecf0f1;
  border-top-color: #3498db;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

.card {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
}
</style>
