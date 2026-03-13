<template>
  <nav class="navbar">
    <div class="container">
      <div class="navbar-brand">
        <router-link to="/" class="logo">
          <span class="logo-icon">🧠</span>
          <span class="logo-text">OpenClaw</span>
        </router-link>
      </div>

      <button class="navbar-toggle" @click="mobileMenuOpen = !mobileMenuOpen">
        <span></span>
        <span></span>
        <span></span>
      </button>

      <ul class="navbar-menu" :class="{ active: mobileMenuOpen }">
        <li>
          <router-link
            to="/summary"
            class="nav-link"
            @click="mobileMenuOpen = false"
          >
            📝 记忆总结
          </router-link>
        </li>
        <li>
          <router-link
            to="/live-stream"
            class="nav-link"
            @click="mobileMenuOpen = false"
          >
            📹 实时直播
          </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const mobileMenuOpen = ref(false)
</script>

<style scoped lang="scss">
.navbar {
  background-color: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  max-width: 1200px;
  margin: 0 auto;
}

.navbar-brand {
  flex-shrink: 0;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  transition: color 0.2s ease;
  text-decoration: none;

  &:hover {
    color: #3498db;
  }

  .logo-icon {
    font-size: 24px;
  }

  .logo-text {
    background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.navbar-menu {
  display: flex;
  list-style: none;
  gap: 24px;
  margin: 0;
  padding: 0;

  li {
    margin: 0;
  }
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  color: #2c3e50;
  font-weight: 500;
  transition: all 0.2s ease;
  border-radius: 4px;
  text-decoration: none;

  &:hover {
    background-color: #f8f9fa;
    color: #3498db;
  }

  &.router-link-active {
    background-color: #f8f9fa;
    color: #3498db;
    font-weight: 600;
  }
}

.navbar-toggle {
  display: none;
  flex-direction: column;
  background: none;
  border: none;
  cursor: pointer;
  gap: 4px;

  span {
    width: 24px;
    height: 2px;
    background-color: #2c3e50;
    border-radius: 2px;
    transition: all 0.2s ease;
  }

  &.active {
    span:nth-child(1) {
      transform: rotate(45deg) translate(8px, 8px);
    }

    span:nth-child(2) {
      opacity: 0;
    }

    span:nth-child(3) {
      transform: rotate(-45deg) translate(7px, -7px);
    }
  }
}

@media (max-width: 768px) {
  .navbar-toggle {
    display: flex;
  }

  .navbar-menu {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    flex-direction: column;
    background-color: #ffffff;
    border-top: 1px solid #ecf0f1;
    gap: 0;
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s ease;

    &.active {
      max-height: 200px;
    }

    li {
      border-bottom: 1px solid #ecf0f1;

      &:last-child {
        border-bottom: none;
      }
    }
  }

  .nav-link {
    display: block;
    padding: 16px;
    border-radius: 0;

    &:hover,
    &.router-link-active {
      background-color: #f8f9fa;
    }
  }
}
</style>
