<script setup lang="ts">
import { inject, ref } from 'vue'
import * as userApi from '@/api/user/index'

const eventList = inject('events')

const confirm = (item) => {
  userApi.updateReminder(item.id).then(() => {
    eventList.value = eventList.value.filter((it) => it.id != item.id)
  })
}
const allConfirmedClick = () => {
  userApi.updateReminderAll().then(() => {
    eventList.value = []
  })
}
</script>

<template>
  <div class="container overflow-auto scroll-hidden">
    <p class="z-10 sticky top-0 mb-2 mt-2 ml-2">
      <button
        @click="allConfirmedClick"
        class="btn btn-sm bg-pcs-second text-white"
        :disabled="eventList.filter((it) => it.status == 0).length == 0"
      >
        All confirmed
      </button>
    </p>
    <ul v-if="eventList?.length != 0" class="mr-4">
      <li
        v-for="item in eventList"
        class="border-b border-solid border-pcs-border pl-2 pb-1 pt-1"
      >
        <p class="flex flex-row items-center gap-x-2">
          <span>{{ item.time }}</span>
          <button
            v-if="item.status == 0"
            class="btn btn-xs bg-pcs-second text-white"
            @click="confirm(item)"
          >
            Confirm
          </button>
        </p>
        <p class="font-bold">{{ item.content }}</p>
      </li>
    </ul>
    <div v-else class="ml-2">
      <p class="text-pcs-primary text-xl">No reminders at the moment</p>
    </div>
  </div>
</template>

<style scoped></style>
