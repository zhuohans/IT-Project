<template>
  <div class="events-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Events</v-app-bar-title>
    </v-app-bar>
    <v-list>
      <v-list-item
        v-for="event in events"
        :key="event.id"
        :subtitle="formatDate(event.notified_datetime)"
      >
        <template v-slot:title>
          <v-chip color="primary">{{ event.name }}</v-chip>
        </template>
        <template v-slot:append>
          <v-btn @click="deleteEvent(event.id)" icon="mdi-delete" size="small" color="red"></v-btn>
        </template>
        <v-divider></v-divider>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
import request from '@/util/request'
import { STATUS_OK } from '@/util/constants'
import { mapActions } from "vuex"
import { formattedDate } from '@/util/dateUtil'
export default {
  name: "Events",
  data() {
    return {
      events: []
    };
  },
  mounted() {
    this.getEvents();
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    async getEvents() {
      const res = await request.get("/events/list");
      // console.log("new collections: ", res.data);
      if (STATUS_OK === res.data.code) {
        this.events = res.data.data.events;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    goBack() {
      this.$router.push("/calendar");
    },
    async deleteEvent(id) {
      const res = await request.delete(`/events/${id}/delete`);
      if (STATUS_OK === res.data.code) {
        this.getEvents();
      } else {
        this.acSetError(res.data.msg);
      }
    },
    formatDate(val) {
      return formattedDate(val)
    },
  }
}
</script>

<style scoped>
  .forum-container {
    height: 100%;
  }
</style>