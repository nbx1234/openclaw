<template>
  <section class="history-shell">
    <div class="history-card card">
      <div class="section-head">
        <div>
          <p class="eyebrow">History</p>
          <h2>历史总结</h2>
        </div>
        <p class="subtext">保留已有记忆记录</p>
      </div>

      <div v-if="loading" class="state-panel">
        <div class="spinner"></div>
        <p>读取历史记录中...</p>
      </div>
      <div v-else-if="records.length === 0" class="state-panel">
        <p>暂无历史记录</p>
      </div>
      <table v-else class="table">
        <thead>
          <tr>
            <th>标题</th>
            <th>摘要</th>
            <th>创建时间</th>
            <th>关联字段</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.id">
            <td>{{ record.title }}</td>
            <td class="content-cell">{{ record.preview }}</td>
            <td>{{ record.timeline.createdAt }}</td>
            <td>
              <span class="mini-chip">{{ labelFor('memoryTypes', record.classifications.memoryType) }}</span>
              <span class="mini-chip">{{ labelFor('priorityLevels', record.classifications.priorityLevel) }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  records: {
    type: Array,
    default: () => []
  },
  options: {
    type: Object,
    default: () => ({})
  }
})

const labelFor = (groupKey, value) => {
  const matched = props.options?.[groupKey]?.find((item) => item.value === value)
  return matched?.label || value
}
</script>

<style scoped lang="scss">
.history-shell {
  max-width: 1400px;
  margin: 24px auto 0;
}

.history-card {
  padding: 24px;
}

.section-head {
  display: flex;
  justify-content: space-between;
  align-items: end;
  gap: 16px;
  margin-bottom: 20px;
}

.eyebrow {
  margin-bottom: 8px;
  color: #7b5a22;
  text-transform: uppercase;
  letter-spacing: 0.14em;
  font-size: 12px;
}

h2 {
  margin: 0;
  color: #1b2530;
}

.subtext {
  color: #6d7782;
}

.state-panel {
  min-height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 12px;
  color: #7f8c8d;
}

.content-cell {
  max-width: 420px;
  color: #6d7782;
}

.mini-chip {
  display: inline-flex;
  margin-right: 8px;
  margin-bottom: 6px;
  padding: 4px 8px;
  border-radius: 999px;
  background: #f8f1e6;
  color: #7b5a22;
  font-size: 12px;
}

@media (max-width: 768px) {
  .section-head {
    flex-direction: column;
    align-items: start;
  }

  .table {
    font-size: 12px;
  }

  .table th,
  .table td {
    padding: 8px;
  }
}
</style>
