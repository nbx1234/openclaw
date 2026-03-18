<template>
  <section class="memory-card">
    <div class="memory-header">
      <div>
        <p class="eyebrow">Persistent Memory</p>
        <h2>{{ config.title || '家庭陪伴记忆字段' }}</h2>
        <p class="subtext">单独调用字段接口，用表格展示家庭陪伴机器人的长期记忆配置。</p>
      </div>
      <div class="stats">
        <div class="stat-item">
          <strong>{{ stats.total || 0 }}</strong>
          <span>总条数</span>
        </div>
        <div class="stat-item">
          <strong>{{ stats.pinned || 0 }}</strong>
          <span>置顶</span>
        </div>
        <div class="stat-item">
          <strong>{{ stats.highPriority || 0 }}</strong>
          <span>高优先级</span>
        </div>
      </div>
    </div>

    <div v-if="loading" class="state-panel">
      <div class="spinner"></div>
      <p>读取家庭陪伴记忆字段中...</p>
    </div>
    <div v-else class="table-shell">
      <div v-if="rows.length === 0" class="state-panel">
        <p>暂无家庭陪伴记忆字段</p>
      </div>
      <table v-else class="table memory-table">
        <thead>
          <tr>
            <th class="drag-col">拖动</th>
            <th>字段</th>
            <th>内容</th>
            <th class="action-col">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(row, index) in rows"
            :key="row.key"
            draggable="true"
            @dragstart="onDragStart(index)"
            @dragover.prevent
            @drop="onDrop(index)"
          >
            <td class="drag-handle">⋮⋮</td>
            <td class="label-cell">{{ row.label }}</td>
            <td class="value-cell">{{ truncateValue(row.value) }}</td>
            <td>
              <button class="btn btn-secondary btn-sm" type="button" @click="selectedRow = row">查看</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="selectedRow" class="modal-overlay" @click="selectedRow = null">
      <div class="modal" @click.stop>
        <div class="modal-head">
          <h3>{{ selectedRow.label }}</h3>
          <button class="text-button" type="button" @click="selectedRow = null">关闭</button>
        </div>
        <p class="key-line">键名：{{ selectedRow.key }}</p>
        <div class="modal-body">{{ selectedRow.value }}</div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  config: {
    type: Object,
    default: () => ({})
  },
  stats: {
    type: Object,
    default: () => ({})
  }
})

const rows = ref([])
const dragIndex = ref(null)
const selectedRow = ref(null)

watch(
  () => props.config?.fields,
  (fields) => {
    rows.value = Array.isArray(fields) ? [...fields] : []
  },
  { immediate: true, deep: true }
)

const onDragStart = (index) => {
  dragIndex.value = index
}

const onDrop = (dropIndex) => {
  if (dragIndex.value === null || dragIndex.value === dropIndex) return
  const next = [...rows.value]
  const [moved] = next.splice(dragIndex.value, 1)
  next.splice(dropIndex, 0, moved)
  rows.value = next
  dragIndex.value = null
}

const truncateValue = (value) => {
  if (!value) return ''
  return value.length > 54 ? `${value.slice(0, 54)}...` : value
}
</script>

<style scoped lang="scss">
.memory-card {
  background: #fffdf8;
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 18px 40px rgba(103, 82, 46, 0.12);
}

.memory-header {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 24px;
}

.eyebrow {
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: #a56b1f;
  margin-bottom: 8px;
}

h2 {
  font-size: 28px;
  margin-bottom: 8px;
}

.subtext {
  max-width: 520px;
  color: #665945;
}

.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(72px, 1fr));
  gap: 12px;
  min-width: 240px;
}

.stat-item {
  background: #fff;
  border: 1px solid #f1e6d2;
  border-radius: 18px;
  padding: 14px;
  text-align: center;
}

.stat-item strong {
  display: block;
  font-size: 24px;
  color: #523514;
}

.stat-item span {
  font-size: 12px;
  color: #8a7355;
}

.table-shell {
  background: #fff;
  border: 1px solid #f1e6d2;
  border-radius: 18px;
  padding: 10px 14px 14px;
}

.memory-table tbody tr {
  cursor: move;
}

.drag-col,
.action-col {
  width: 72px;
}

.drag-handle {
  color: #a56b1f;
  font-size: 18px;
  text-align: center;
}

.label-cell {
  font-weight: 600;
  color: #523514;
  white-space: nowrap;
}

.value-cell {
  color: #665945;
}

.state-panel {
  min-height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 12px;
  color: #8a7355;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(23, 16, 9, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  z-index: 1000;
}

.modal {
  width: min(720px, 100%);
  background: #fffefb;
  border-radius: 24px;
  padding: 24px;
}

.modal-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  margin-bottom: 14px;
}

.key-line {
  color: #8a7355;
  margin-bottom: 12px;
}

.modal-body {
  white-space: pre-wrap;
  line-height: 1.8;
  color: #372f24;
}

.text-button {
  background: none;
  border: none;
  color: #285ea8;
  padding: 0;
}

@media (max-width: 960px) {
  .memory-header {
    flex-direction: column;
  }

  .stats {
    min-width: 0;
    grid-template-columns: 1fr;
  }
}
</style>
