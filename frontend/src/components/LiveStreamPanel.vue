<template>
  <section class="stream-card">
    <div class="card-head">
      <div>
        <p class="eyebrow">Livestream</p>
        <h2>实时画面</h2>
      </div>
      <div class="actions">
        <button class="btn btn-secondary btn-sm" @click="refreshStream" :disabled="loading">刷新</button>
        <button class="btn btn-primary btn-sm" @click="toggleFullscreen">全屏</button>
      </div>
    </div>

    <div ref="streamWrapper" class="stream-wrapper">
      <img
        :key="imageKey"
        :src="streamUrl"
        alt="Live Stream"
        class="stream-image"
        @error="handleImageError"
        @load="handleImageLoad"
      />
      <div v-if="loading" class="stream-overlay">
        <div class="spinner"></div>
        <p>连接视频流中...</p>
      </div>
      <div v-if="error" class="stream-overlay error-state">
        <p>{{ error }}</p>
        <p class="hint">{{ retryCountdown }} 秒后自动重连</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const streamUrl = ref('http://127.0.0.1:8888/stream')
const loading = ref(false)
const error = ref(null)
const retryCountdown = ref(0)
const imageKey = ref(0)
const retryTimer = ref(null)
const streamWrapper = ref(null)

const clearRetryTimer = () => {
  if (retryTimer.value) {
    clearInterval(retryTimer.value)
    retryTimer.value = null
  }
}

const handleImageLoad = () => {
  loading.value = false
  error.value = null
  retryCountdown.value = 0
  clearRetryTimer()
}

const handleImageError = () => {
  loading.value = false
  error.value = '视频流加载失败'
  startAutoRetry()
}

const startAutoRetry = () => {
  clearRetryTimer()
  retryCountdown.value = 5
  retryTimer.value = setInterval(() => {
    retryCountdown.value -= 1
    if (retryCountdown.value <= 0) {
      clearRetryTimer()
      refreshStream()
    }
  }, 1000)
}

const refreshStream = () => {
  loading.value = true
  error.value = null
  imageKey.value += 1
}

const toggleFullscreen = () => {
  if (!streamWrapper.value) return

  if (document.fullscreenElement) {
    document.exitFullscreen()
    return
  }

  streamWrapper.value.requestFullscreen().catch((fullscreenError) => {
    console.error('Fullscreen request failed:', fullscreenError)
  })
}

onMounted(() => {
  loading.value = true
})

onUnmounted(() => {
  clearRetryTimer()
})
</script>

<style scoped lang="scss">
.stream-card {
  background: linear-gradient(180deg, #15202b 0%, #091119 100%);
  color: #f3f6f8;
  border-radius: 24px;
  padding: 20px;
  box-shadow: 0 20px 50px rgba(9, 17, 25, 0.35);
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 16px;
}

.eyebrow {
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: #84c5ff;
  margin-bottom: 8px;
}

h2 {
  font-size: 24px;
  margin: 0;
}

.actions {
  display: flex;
  gap: 8px;
}

.stream-wrapper {
  position: relative;
  border-radius: 18px;
  overflow: hidden;
  min-height: 320px;
  background: #000;
}

.stream-image {
  display: block;
  width: 100%;
  aspect-ratio: 16 / 9;
  object-fit: contain;
}

.stream-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: rgba(0, 0, 0, 0.72);

  p {
    margin: 0;
  }
}

.error-state {
  color: #ffd2cf;
}

.hint {
  font-size: 13px;
  opacity: 0.8;
}

@media (max-width: 768px) {
  .card-head {
    flex-direction: column;
  }

  .actions {
    width: 100%;
  }

  .actions .btn {
    flex: 1;
    justify-content: center;
  }

  .stream-wrapper {
    min-height: 220px;
  }
}
</style>
