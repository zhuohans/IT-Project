<template>
  <div class="profile-container">
    <v-app-bar color="primary">
      <v-app-bar-title>Profile</v-app-bar-title>
    </v-app-bar>
    <v-card style="width: 100%;height: 100%;">
      <v-list v-if="profile">
        <v-list-item
          :prepend-avatar="`${host}${profile.avatar}`"
          :subtitle="profile.email"
          :title="profile.username"
        >
        </v-list-item>
      </v-list>
  
      <v-divider></v-divider>
  
      <v-list
        :lines="false"
        density="compact"
        nav
      >
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :value="item"
          color="primary"
        >
          <template v-slot:prepend>
            <v-icon :icon="item.icon"></v-icon>
          </template>
  
          <v-list-item-title v-text="item.text"></v-list-item-title>
          <template v-slot:append>
            <v-btn
              color="grey-lighten-1"
              icon="mdi-arrow-right"
              variant="text"
              @click="item.callback"
            ></v-btn>
          </template>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex"
import request from '@/util/request'
import { STATUS_OK, HOST } from '@/util/constants'
import { clear } from '@/util/cache'
export default {
  name: "Profile",
  computed: {
    ...mapState('user', ['username', 'token']),
  },
  data() {
    return {
      host: HOST,
      items: [
        { text: 'Edit Profile', icon: 'mdi-pencil', callback: () => { this.$router.push("/profile/edit"); } },
        { text: 'Upload avatar', icon: 'mdi-upload', callback: () => { this.$router.push("/avatar/upload"); } },
        { text: 'Reset Password', icon: 'mdi-lock-reset', callback: () => { this.$router.push("/resetPassword"); } },
        { text: 'Collections', icon: 'mdi-star', callback: () => { this.$router.push("/collections"); } },
        { text: 'Logout', icon: 'mdi-logout', callback: () => { 
          clear();
          this.$router.push("/login");
         } 
        },
      ],
      profile: null,
    };
  },
  mounted() {
    this.auth();
    this.getProfile();
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    auth() {
      if (!this.token) {
        // console.log('token', this.token);
        this.$router.push('/login');
      }
    },
    async getProfile() {
      const res = await request.get("/user/profile");
      if (STATUS_OK === res.data.code) {
        this.profile = res.data.data.user;
      } else {
        console.log(res);
        this.acSetError(res.data.msg);
      }
    }
  }
}
</script>

<style>

</style>