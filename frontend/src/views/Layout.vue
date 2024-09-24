<template>
  <v-app class="app-container">
    <v-main class="app-main-container">
      <router-view/>
      <Notification />
    </v-main>
    <AppFooter class="app-footer" />
  </v-app>
</template>
<script>
import AppFooter from '@/components/AppFooter.vue'
import Notification from '@/components/notification'

export default {
  components: { AppFooter, Notification },
  name: 'Layout'
}
</script>

<style scoped>
  .app-container {
    position: relative;
  }
  .app-main-container {
    height: 90vh;
    max-height: 90vh;
    overflow-y: scroll;
  }
  .app-footer {
    position: absolute;
    bottom: 0;
    left: 0;
    height: 10vh;
  }
</style>
