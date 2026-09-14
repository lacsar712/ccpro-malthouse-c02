<template>
  <div>
    <div class="toolbar">
      <div>
        <h2 class="page-title">仪表盘</h2>
        <p class="page-sub">发酵罐与批次运行概览</p>
      </div>
    </div>

    <div class="stats">
      <div class="card stat">
        <div class="label">发酵罐总数</div>
        <div class="value">{{ data.fermenterCount ?? '-' }}</div>
      </div>
      <div class="card stat">
        <div class="label">进行中批次</div>
        <div class="value">{{ data.activeBatchCount ?? '-' }}</div>
      </div>
      <div class="card stat">
        <div class="label">近 7 日读数</div>
        <div class="value">{{ data.readingsLast7Days ?? '-' }}</div>
      </div>
    </div>

    <div class="card">
      <h3 style="margin-top:0">按状态批次计数</h3>
      <table class="table">
        <thead>
          <tr>
            <th>状态</th>
            <th>数量</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in statusRows" :key="row.key">
            <td><span class="tag">{{ row.label }}</span></td>
            <td>{{ row.count }}</td>
          </tr>
        </tbody>
      </table>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import api from '../api/http'

const data = reactive({
  fermenterCount: null,
  activeBatchCount: null,
  readingsLast7Days: null,
  batchesByStatus: {}
})
const error = ref('')

const labels = {
  planned: 'planned 计划中',
  active: 'active 发酵中',
  packaged: 'packaged 已包装',
  dumped: 'dumped 已倒罐'
}

const statusRows = computed(() =>
  Object.keys(labels).map((key) => ({
    key,
    label: labels[key],
    count: data.batchesByStatus?.[key] ?? 0
  }))
)

async function load() {
  error.value = ''
  try {
    const { data: res } = await api.get('/dashboard')
    Object.assign(data, res)
  } catch (e) {
    error.value = e.response?.data?.message || '加载失败'
  }
}

onMounted(load)
</script>
