<template>
  <div>
    <v-toolbar color="primary" style="margin-bottom: 1rem;">
      <v-btn icon="mdi-menu" variant="text" @click="goEvents"></v-btn>

      <v-toolbar-title>Calendar</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    <v-row justify="space-around">
      <v-date-picker
        color="primary"
        v-model="created_at"
      ></v-date-picker>
    </v-row>
    <v-text-field
      v-model="eventName"
      label="Event Name"
      v-if="token"
    ></v-text-field>
    <v-text-field
      v-model="time"
      label="Time"
      type="time"
      v-if="token"
    ></v-text-field>
    <v-btn v-if="token" @click="addEvent()" color="primary" block>Add event</v-btn>
  </div>
</template>

<script>
import { getDate } from '@/util/dateUtil'
import request from '@/util/request'
import { STATUS_OK } from '@/util/constants'
import { mapActions, mapState } from "vuex"
export default {
  name: "Calendar",
  data() {
    return {
      created_at: null,
      time: null,
      eventName: '',
    };
  },
  computed: {
    ...mapState('user', ['token'])
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    async addEvent() {
      console.log(getDate(this.created_at),this.time,this.eventName);
      const created_at = this.created_at;
      const time = this.time;
      const eventName = this.eventName;
      if (!created_at) {
        this.acSetError("Please choose date");
        return;
      }
      if (!time) {
        this.acSetError("Please choose time");
        return;
      }
      if (!eventName) {
        this.acSetError("Please enter event name");
        return;
      }
      const data = { notified_datetime: `${getDate(created_at)} ${time}:00`, name: eventName };
      const res = await request.post('/events/create', data);
      if (STATUS_OK === res.data.code) {
        this.acSetError('Create event successfully.');
        this.$router.push('/events');
      } else {
        this.acSetError(res.data.msg);
      }
    },
    goEvents() {
      this.$router.push("/events");
    }
  }
}
</script>

<style>

</style>