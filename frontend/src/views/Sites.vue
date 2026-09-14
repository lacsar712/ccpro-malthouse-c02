<template>
  <div>
    <div class="toolbar">
      <div>
        <h2 class="page-title">酿造厂区</h2>
        <p class="page-sub">管理厂区名称、位置与备注</p>
      </div>
      <button class="btn" @click="openCreate">新增厂区</button>
    </div>

    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>名称</th>
            <th>位置</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.name }}</td>
            <td>{{ item.location || '-' }}</td>
            <td>{{ item.notes || '-' }}</td>
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
        <h3>{{ form.id ? '编辑厂区' : '新增厂区' }}</h3>
        <div class="form-grid">
          <label class="full">
            名称
            <input v-model="form.name" />
          </label>
          <label class="full">
            位置
            <input v-model="form.location" />
          </label>
          <label class="full">
            备注
            <textarea v-model="form.notes" rows="3" />
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
const error = ref('')
const formError = ref('')
const showModal = ref(false)
const form = reactive({ id: null, name: '', location: '', notes: '' })

async function load() {
  error.value = ''
  try {
    const { data } = await api.get('/sites')
    list.value = data
  } catch (e) {
    error.value = e.response?.data?.message || '加载失败'
  }
}

function openCreate() {
  Object.assign(form, { id: null, name: '', location: '', notes: '' })
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
    const payload = { name: form.name, location: form.location, notes: form.notes }
    if (form.id) {
      await api.put(`/sites/${form.id}`, payload)
    } else {
      await api.post('/sites', payload)
    }
    showModal.value = false
    await load()
  } catch (e) {
    formError.value = e.response?.data?.message || '保存失败'
  }
}

async function remove(item) {
  if (!confirm(`确认删除厂区「${item.name}」？`)) return
  try {
    await api.delete(`/sites/${item.id}`)
    await load()
  } catch (e) {
    error.value = e.response?.data?.message || '删除失败'
  }
}

onMounted(load)
</script>
