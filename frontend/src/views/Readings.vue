<template>
  <div>
    <div class="toolbar">
      <div>
        <h2 class="page-title">比重读数</h2>
        <p class="page-sub">记录发酵过程中的比重与温度</p>
      </div>
      <button class="btn" @click="openCreate">新增读数</button>
    </div>

    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>批次</th>
            <th>测量时间</th>
            <th>比重</th>
            <th>温度 ℃</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ batchLabel(item.batchId) }}</td>
            <td>{{ formatTime(item.measuredAt) }}</td>
            <td>{{ item.specificGravity }}</td>
            <td>{{ item.temperatureC }}</td>
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
        <h3>{{ form.id ? '编辑读数' : '新增读数' }}</h3>
        <div class="form-grid">
          <label class="full">
            批次
            <select v-model.number="form.batchId">
              <option v-for="b in batches" :key="b.id" :value="b.id">{{ b.recipeName }} (#{{ b.id }})</option>
            </select>
          </label>
          <label>
            测量时间
            <input v-model="form.measuredAt" type="datetime-local" />
          </label>
          <label>
            比重 SG
            <input v-model.number="form.specificGravity" type="number" step="0.001" />
          </label>
          <label>
            温度 ℃
            <input v-model.number="form.temperatureC" type="number" step="0.1" />
          </label>
          <label class="full">
            备注
            <input v-model="form.notes" />
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
const batches = ref([])
const error = ref('')
const formError = ref('')
const showModal = ref(false)
const form = reactive({
  id: null,
  batchId: null,
  measuredAt: '',
  specificGravity: 1.02,
  temperatureC: 18,
  notes: ''
})

function batchLabel(id) {
  const b = batches.value.find((x) => x.id === id)
  return b ? `${b.recipeName} (#${b.id})` : id
}

function formatTime(v) {
  if (!v) return '-'
  return String(v).replace('T', ' ').slice(0, 19)
}

function toLocalInput(v) {
  if (!v) return ''
  const d = new Date(v)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

function fromLocalInput(v) {
  if (!v) return null
  return v.length === 16 ? `${v}:00` : v
}

async function load() {
  error.value = ''
  try {
    const [r, b] = await Promise.all([api.get('/readings'), api.get('/batches')])
    list.value = r.data
    batches.value = b.data
  } catch (e) {
    error.value = e.response?.data?.message || '加载失败'
  }
}

function openCreate() {
  Object.assign(form, {
    id: null,
    batchId: batches.value[0]?.id || null,
    measuredAt: toLocalInput(new Date().toISOString()),
    specificGravity: 1.02,
    temperatureC: 18,
    notes: ''
  })
  formError.value = ''
  showModal.value = true
}

function openEdit(item) {
  Object.assign(form, {
    ...item,
    measuredAt: toLocalInput(item.measuredAt)
  })
  formError.value = ''
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    const payload = {
      batchId: form.batchId,
      measuredAt: fromLocalInput(form.measuredAt),
      specificGravity: form.specificGravity,
      temperatureC: form.temperatureC,
      notes: form.notes
    }
    if (form.id) {
      await api.put(`/readings/${form.id}`, payload)
    } else {
      await api.post('/readings', payload)
    }
    showModal.value = false
    await load()
  } catch (e) {
    formError.value = e.response?.data?.message || '保存失败'
  }
}

async function remove(item) {
  if (!confirm('确认删除该读数？')) return
  try {
    await api.delete(`/readings/${item.id}`)
    await load()
  } catch (e) {
    error.value = e.response?.data?.message || '删除失败'
  }
}

onMounted(load)
</script>
