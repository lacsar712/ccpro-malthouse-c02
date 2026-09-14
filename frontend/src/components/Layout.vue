<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">
        <h1>MaltHouse</h1>
        <p>麦汁发酵罐台账</p>
      </div>
      <nav class="nav">
        <router-link to="/dashboard">仪表盘</router-link>
        <router-link to="/sites">酿造厂区</router-link>
        <router-link to="/fermenters">发酵罐</router-link>
        <router-link to="/batches">发酵批次</router-link>
        <router-link to="/readings">比重读数</router-link>
      </nav>
      <div class="sidebar-footer">
        <div>{{ auth.user?.displayName }} · {{ auth.roleLabel }}</div>
        <button class="btn secondary small" style="margin-top: 0.6rem" @click="onLogout">退出登录</button>
      </div>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()

function onLogout() {
  auth.logout()
  router.push('/login')
}
</script>
