<script setup lang="ts">
const prop = defineProps({ text: String })
</script>

<template>
  <div
    class="w-screen h-screen flex flex-row justify-center items-center glass"
  >
    <span class="loading loading-ring loading-xs"></span>
    <span class="loading loading-ring loading-sm"></span>
    <span class="loading loading-ring loading-md"></span>
    <span class="loading loading-ring loading-lg"></span>
    <span>{{ prop.text }}</span>
  </div>
</template>

<style scoped></style>
