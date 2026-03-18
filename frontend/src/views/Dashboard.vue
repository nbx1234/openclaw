<template>
  <main class="dashboard">
    <section class="hero">
      <div class="hero-copy">
        <p class="eyebrow">OpenClaw Workspace</p>
        <h1>🏠家庭陪伴机器人工作台</h1>
      </div>
    </section>

    <section class="top-grid">
      <LiveStreamPanel />
      <PersistentMemoryPanel :loading="loading" :config="persistentMemoryConfig" :stats="stats" />
    </section>

    <MemoryHistorySection :loading="loading" :records="records" :options="fieldLabelOptions" />
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getPersistentMemoryFields, getPersistentMemoryPayload } from '../api/index.js'
import LiveStreamPanel from '../components/LiveStreamPanel.vue'
import MemoryHistorySection from '../components/MemoryHistorySection.vue'
import PersistentMemoryPanel from '../components/PersistentMemoryPanel.vue'

const persistentMemoryConfig = ref({
  title: '',
  fields: []
})
const fieldLabelOptions = ref({
  memoryTypes: [],
  priorityLevels: [],
  retentionLevels: [],
  sourceTypes: []
})
const stats = ref({})
const records = ref([])
const loading = ref(false)

const fetchDashboardData = async () => {
  loading.value = true
  try {
    const [historyResponse, fieldsResponse] = await Promise.all([
      getPersistentMemoryPayload(),
      getPersistentMemoryFields()
    ])

    persistentMemoryConfig.value = {
      title: '',
      fields: [],
      ...(fieldsResponse.data || {})
    }
    stats.value = historyResponse.data?.stats || {}
    records.value = historyResponse.data?.records || []
    fieldLabelOptions.value = {
      memoryTypes: [],
      priorityLevels: [],
      retentionLevels: [],
      sourceTypes: [],
      ...(historyResponse.data?.fieldLabelOptions || {})
    }
  } catch (error) {
    console.error('Failed to fetch dashboard data:', error)
    persistentMemoryConfig.value = {
      title: '',
      fields: []
    }
    fieldLabelOptions.value = {
      memoryTypes: [],
      priorityLevels: [],
      retentionLevels: [],
      sourceTypes: []
    }
    stats.value = {}
    records.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped lang="scss">
.dashboard {
  min-height: 100vh;
  padding: 32px 24px 48px;
  background:
    radial-gradient(circle at top left, rgba(255, 215, 154, 0.45), transparent 28%),
    radial-gradient(circle at top right, rgba(127, 181, 255, 0.3), transparent 24%),
    linear-gradient(180deg, #f8f4eb 0%, #eef3f7 100%);
}

.hero {
  max-width: 1400px;
  margin: 0 auto 24px;
}

.hero-copy {
  max-width: 760px;
}

.eyebrow {
  margin-bottom: 12px;
  color: #7b5a22;
  text-transform: uppercase;
  letter-spacing: 0.14em;
  font-size: 12px;
}

h1 {
  font-size: clamp(36px, 5vw, 64px);
  line-height: 0.96;
  margin-bottom: 16px;
  color: #1b2530;
}

.lead {
  font-size: 18px;
  color: #4c5d6d;
  max-width: 620px;
}

.top-grid {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: minmax(420px, 0.9fr) minmax(560px, 1.1fr);
  gap: 24px;
  align-items: start;
}

@media (max-width: 1180px) {
  .top-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .dashboard {
    padding: 20px 14px 32px;
  }

  .lead {
    font-size: 16px;
  }
}
</style>
