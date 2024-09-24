<template>
  <v-layout class="overflow-visible" style="height: 56px;">
    <v-bottom-navigation
      v-model="value"
      color="teal"
      bg-color="rgb(230, 208, 183)"
      grow
    >
      <v-btn @click="go('/index')">
        <v-icon>mdi-home</v-icon>
        Home
      </v-btn>

      <v-btn @click="go('/forum')">
        <v-icon>mdi-forum-outline</v-icon>
        Forum
      </v-btn>

      <v-btn @click="go('/calendar')">
        <v-icon>mdi-calendar</v-icon>
        Calendar
      </v-btn>

      <v-btn @click="go('/profile')">
        <v-icon>mdi-account-outline</v-icon>
        Profile
      </v-btn>
    </v-bottom-navigation>
  </v-layout>
</template>

<script>
 export default {
    data: () => ({ 
      value: 0,
      isAbsolute: true
    }),
    methods: {
      go(path) {
        this.$router.push(path);
      } 
    }
  }
</script>

<style scoped lang="css">
  .bottom-nav-bar-bg {
    background-color: rgb(230, 208, 183);
  }
</style>
