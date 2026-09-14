<template>
  <div>
    <div class="toolbar">
      <div>
        <h2 class="page-title">发酵罐</h2>
        <p class="page-sub">罐号在同一厂区内唯一</p>
      </div>
      <button class="btn" @click="openCreate">新增发酵罐</button>
    </div>

    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>罐号</th>
            <th>厂区</th>
            <th>容量 (L)</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.tankCode }}</td>
            <td>{{ siteName(item.siteId) }}</td>
            <td>{{ item.capacityLiters }}</td>
            <td><span class="tag">{{ item.status }}</span></td>
            <td>
              <button class="btn secondary small" @click="openEdit(item)">编辑</button>
              <button class="btn danger small" @click="remove(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="!list.length" class="page-sub">暂无数据</p>
      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? '编辑发酵罐' : '新增发酵罐' }}</h3>
        <div class="form-grid">
          <label>
            厂区
            <select v-model.number="form.siteId">
              <option v-for="s in sites" :key="s.id" :value="s.id">{{ s.name }}</option>
            </select>
          </label>
          <label>
            罐号
            <input v-model="form.tankCode" />
          </label>
          <label>
            容量 (升)
            <input v-model.number="form.capacityLiters" type="number" step="1" min="1" />
          </label>
          <label>
            状态
            <select v-model="form.status">
              <option value="idle">idle</option>
              <option value="fermenting">fermenting</option>
              <option value="cip">cip</option>
            </select>
          </label>
        </div>
        <p v-if="formError" class="error">{{ formError }}</p>
        <div class="modal-actions">
          <button class="btn secondary" @click="showModal = false">取消</button>
          <button class="btn" @click="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import api from '../api/http'

const list = ref([])
const sites = ref([])
const error = ref('')
const formError = ref('')
const showModal = ref(false)
const form = reactive({
  id: null,
  siteId: null,
  tankCode: '',
  capacityLiters: 1000,
  status: 'idle'
})

function siteName(id) {
  return sites.value.find((s) => s.id === id)?.name || id
}

async function load() {
  error.value = ''
  try {
    const [f, s] = await Promise.all([api.get('/fermenters'), api.get('/sites')])
    list.value = f.data
    sites.value = s.data
  } catch (e) {
    error.value = e.response?.data?.message || '加载失败'
  }
}

function openCreate() {
  Object.assign(form, {
    id: null,
    siteId: sites.value[0]?.id || null,
    tankCode: '',
    capacityLiters: 1000,
    status: 'idle'
  })
  formError.value = ''
  showModal.value = true
}

function openEdit(item) {
  Object.assign(form, item)
  formError.value = ''
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    const payload = {
      siteId: form.siteId,
      tankCode: form.tankCode,
      capacityLiters: form.capacityLiters,
      status: form.status
    }
    if (form.id) {
      await api.put(`/fermenters/${form.id}`, payload)
    } else {
      await api.post('/fermenters', payload)
    }
    showModal.value = false
    await load()
  } catch (e) {
    formError.value = e.response?.data?.message || '保存失败'
  }
}

async function remove(item) {
  if (!confirm(`确认删除发酵罐「${item.tankCode}」？`)) return
  try {
    await api.delete(`/fermenters/${item.id}`)
    await load()
  } catch (e) {
    error.value = e.response?.data?.message || '删除失败'
  }
}

onMounted(load)
</script>
