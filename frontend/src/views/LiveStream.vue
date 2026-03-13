<template>
  <div class="live-stream-page">
    <header class="page-header">
      <div class="container">
        <h1>实时直播</h1>
        <p class="subtitle">实时视频流展示</p>
      </div>
    </header>

    <main class="page-content">
      <div class="stream-container">
        <div class="stream-wrapper">
          <div class="stream-player">
            <img
              :key="imageKey"
              :src="streamUrl"
              alt="Live Stream"
              class="stream-image"
              @error="handleImageError"
              @load="handleImageLoad"
            />
            <div v-if="loading" class="stream-loading">
              <div class="spinner"></div>
              <p>加载中...</p>
            </div>
            <div v-if="error" class="stream-error">
              <p>{{ error }}</p>
              <p class="error-hint">{{ retryCountdown }}秒后自动重连...</p>
            </div>
          </div>

          <div class="stream-controls">
            <button
              class="btn btn-primary"
              @click="refreshStream"
              :disabled="loading"
            >
              <span>🔄</span>
              刷新
            </button>
            <button
              class="btn btn-primary"
              @click="toggleFullscreen"
            >
              <span>⛶</span>
              全屏
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
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

const handleImageLoad = () => {
  loading.value = false
  error.value = null
  retryCountdown.value = 0
  if (retryTimer.value) {
    clearTimeout(retryTimer.value)
  }
}

const handleImageError = () => {
  loading.value = false
  error.value = '视频加载失败'
  startAutoRetry()
}

const startAutoRetry = () => {
  if (retryTimer.value) {
    clearTimeout(retryTimer.value)
  }

  retryCountdown.value = 5
  const countdown = setInterval(() => {
    retryCountdown.value--
    if (retryCountdown.value <= 0) {
      clearInterval(countdown)
      refreshStream()
    }
  }, 1000)

  retryTimer.value = countdown
}

const refreshStream = () => {
  loading.value = true
  error.value = null
  imageKey.value++
}

const toggleFullscreen = () => {
  if (streamWrapper.value) {
    if (document.fullscreenElement) {
      document.exitFullscreen()
    } else {
      streamWrapper.value.requestFullscreen().catch(err => {
        console.error('Fullscreen request failed:', err)
      })
    }
  }
}

onMounted(() => {
  loading.value = true
})

onUnmounted(() => {
  if (retryTimer.value) {
    clearTimeout(retryTimer.value)
  }
})
</script>

<style scoped lang="scss">
.live-stream-page {
  min-height: 100vh;
  background-color: #000;
  display: flex;
  flex-direction: column;
}

.page-header {
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
  color: white;
  padding: 24px 16px;

  h1 {
    font-size: 28px;
    font-weight: 700;
    margin-bottom: 8px;
  }

  .subtitle {
    font-size: 14px;
    opacity: 0.9;
  }
}

.page-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.stream-container {
  width: 100%;
  max-width: 1200px;
}

.stream-wrapper {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stream-player {
  position: relative;
  background-color: #000;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
  aspect-ratio: 16 / 9;
  display: flex;
  align-items: center;
  justify-content: center;

  .stream-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    display: block;
  }

  .stream-loading,
  .stream-error {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: rgba(0, 0, 0, 0.8);
    color: white;
    z-index: 10;
  }

  .stream-loading {
    p {
      margin-top: 16px;
      font-size: 16px;
    }
  }

  .stream-error {
    p {
      margin: 0;
      font-size: 16px;

      &.error-hint {
        margin-top: 8px;
        font-size: 14px;
        opacity: 0.8;
      }
    }
  }
}

.stream-controls {
  display: flex;
  gap: 16px;
  justify-content: center;

  .btn {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 16px 24px;
    font-size: 16px;
    font-weight: 500;
    background-color: #3498db;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover:not(:disabled) {
      background-color: #2980b9;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    span {
      font-size: 18px;
    }
  }
}

@media (max-width: 768px) {
  .page-header {
    h1 {
      font-size: 20px;
    }

    .subtitle {
      font-size: 12px;
    }
  }

  .page-content {
    padding: 16px;
  }

  .stream-controls {
    flex-direction: column;

    .btn {
      width: 100%;
      justify-content: center;
    }
  }
}

:fullscreen {
  background-color: #000;

  .stream-player {
    border-radius: 0;
    aspect-ratio: auto;
  }
}
</style>
