<template>
  <div>
    <div class="toolbar">
      <div>
        <h2 class="page-title">发酵批次</h2>
        <p class="page-sub">配方、比重目标与批次状态</p>
      </div>
      <button class="btn" @click="openCreate">新增批次</button>
    </div>

    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>配方</th>
            <th>发酵罐</th>
            <th>酿造日</th>
            <th>OG / 目标 FG</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.recipeName }}</td>
            <td>{{ tankLabel(item.fermenterId) }}</td>
            <td>{{ item.brewDate }}</td>
            <td>{{ item.originalGravity }} / {{ item.targetFg }}</td>
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
        <h3>{{ form.id ? '编辑批次' : '新增批次' }}</h3>
        <div class="form-grid">
          <label>
            发酵罐
            <select v-model.number="form.fermenterId">
              <option v-for="f in fermenters" :key="f.id" :value="f.id">{{ f.tankCode }}</option>
            </select>
          </label>
          <label>
            配方名
            <input v-model="form.recipeName" />
          </label>
          <label>
            酿造日期
            <input v-model="form.brewDate" type="date" />
          </label>
          <label>
            状态
            <select v-model="form.status">
              <option value="planned">planned</option>
              <option value="active">active</option>
              <option value="packaged">packaged</option>
              <option value="dumped">dumped</option>
            </select>
          </label>
          <label>
            初始比重 OG
            <input v-model.number="form.originalGravity" type="number" step="0.001" />
          </label>
          <label>
            目标终比重 FG
            <input v-model.number="form.targetFg" type="number" step="0.001" />
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
const fermenters = ref([])
const error = ref('')
const formError = ref('')
const showModal = ref(false)
const form = reactive({
  id: null,
  fermenterId: null,
  recipeName: '',
  brewDate: '',
  originalGravity: 1.05,
  targetFg: 1.01,
  status: 'planned'
})

function tankLabel(id) {
  return fermenters.value.find((f) => f.id === id)?.tankCode || id
}

async function load() {
  error.value = ''
  try {
    const [b, f] = await Promise.all([api.get('/batches'), api.get('/fermenters')])
    list.value = b.data
    fermenters.value = f.data
  } catch (e) {
    error.value = e.response?.data?.message || '加载失败'
  }
}

function openCreate() {
  Object.assign(form, {
    id: null,
    fermenterId: fermenters.value[0]?.id || null,
    recipeName: '',
    brewDate: new Date().toISOString().slice(0, 10),
    originalGravity: 1.05,
    targetFg: 1.01,
    status: 'planned'
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
      fermenterId: form.fermenterId,
      recipeName: form.recipeName,
      brewDate: form.brewDate,
      originalGravity: form.originalGravity,
      targetFg: form.targetFg,
      status: form.status
    }
    if (form.id) {
      await api.put(`/batches/${form.id}`, payload)
    } else {
      await api.post('/batches', payload)
    }
    showModal.value = false
    await load()
  } catch (e) {
    formError.value = e.response?.data?.message || '保存失败'
  }
}

async function remove(item) {
  if (!confirm(`确认删除批次「${item.recipeName}」？`)) return
  try {
    await api.delete(`/batches/${item.id}`)
    await load()
  } catch (e) {
    error.value = e.response?.data?.message || '删除失败'
  }
}

onMounted(load)
</script>
