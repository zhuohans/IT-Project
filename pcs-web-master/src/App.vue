<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { provide, ref } from 'vue'

const theme = ref('light')
provide('theme', theme)
</script>

<template>
  <div class="w-screen h-screen overflow-hidden" :data-theme="theme">
    <router-view />
  </div>
</template>

<style scoped></style>
