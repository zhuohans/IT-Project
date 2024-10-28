<script setup lang="ts">
import { ref } from 'vue'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import timeGridPlugin from '@fullcalendar/timegrid'
import * as userApi from '@/api/user/index'
import { formatDateTime } from '@/utils/CommonUtil'
import { mdiClose } from '@mdi/js'
import rrulePlugin from '@fullcalendar/rrule'
import { useRouter } from 'vue-router'

const router = useRouter()

const calendarOptions = ref({
  plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin, rrulePlugin],
  initialView: 'dayGridMonth',
  headerToolbar: {
    // left: 'dayGridMonth,timeGridWeek,timeGridDay myCustomButton',
    left: 'dayGridMonth,timeGridWeek,timeGridDay',
    center: 'title',
    right: 'prevYear,prev,next,nextYear today',
  },
  allDaySlot: false,
  editable: false,
  eventResizableFromStart: false,
  height: 600,
  events: [],
  eventColor: '#378006',
  eventClick: (info) => {
    console.log(info)
    info.el.style.borderColor = 'red'
    eventClick(info)
  },
  dateClick: (info) => {
    dateClick(info)
    // info.dayEl.style.backgroundColor = 'red'
  },
  dayMaxEventRows: 2,
})

const addReminderDialog = ref()
const reminder = ref({
  title: '',
  start: '',
  end: '',
  date: null,
})

const deleteReminderDialog = ref()
const currentInfo = ref()
const eventClick = (info) => {
  currentInfo.value = info
  console.log(currentInfo.value, 'iiii')
  deleteReminderDialog.value.showModal()
}

const dateClick = (info) => {
  currentInfo.value = info
  reminder.value.start = info.dateStr
  reminder.value.end = info.dateStr
  reminder.value.date = info.date
  addReminderDialog.value.showModal()
}

const addReminderSave = () => {
  let temps = generateRecurringEvents(
    currentInfo.value,
    model.value,
    reminder.value.title,
    '',
  )
  let temp = JSON.parse(JSON.stringify(reminder.value))
  userApi
    .createReminder({
      content: temp.title,
      time: formatDateTime(reminder.value.date),
      model: model.value,
    })
    .then((res) => {
      for (let item of temps) {
        calendarOptions.value.events.push(item)
      }
      router.go(0)
    })
}
userApi.getReminderList().then((res) => {
  if (res?.length != 0) {
    for (let item of res) {
      let events = generateRecurringEvents(
        { dateStr: item.time, date: new Date(item.time) },
        item.model,
        item.content,
        item.id,
      )
      calendarOptions.value.events = calendarOptions.value.events.concat(events)
    }
  }
})
const deleteReminder = () => {
  userApi
    .deleteReminder(currentInfo.value.event._def.extendedProps.eventId)
    .then((res) => {
      calendarOptions.value.events = calendarOptions.value.events.filter(
        (it) =>
          it.eventId != currentInfo.value.event._def.extendedProps.eventId,
      )
      currentInfo.value.event.remove()
      currentInfo.value = null
      deleteReminderDialog.value.close()
    })
}

const models = [
  { id: 0, title: 'Please select a mode' },
  { id: 1, title: 'Only once' },
  { id: 2, title: 'Every day' },
  { id: 3, title: 'Every week' },
  { id: 4, title: 'Every month' },
]

const model = ref(0)

const generateRecurringEvents = (info, modeId, title, id) => {
  const events = []
  let currentDate = new Date(info.dateStr)
  console.log(info.dateStr, currentDate)

  switch (modeId) {
    case 1: // Only once
      events.push({
        start: info.dateStr,
        end: info.dateStr,
        date: info.date,
        title: title,
        eventId: id,
      })
      break

    case 2: // Every day
      events.push({
        start: info.dateStr,
        end: info.dateStr,
        date: info.date,
        title: title,
        eventId: id,
        rrule: {
          freq: 'daily',
          dtstart: new Date(currentDate).toISOString(), // 开始日期
          interval: 1, // 频率间隔
        },
      })
      break

    case 3: // Every week
      events.push({
        start: new Date(currentDate).toISOString(),
        end: new Date(currentDate).toISOString(),
        date: currentDate,
        title: title,
        eventId: id,
        rrule: {
          freq: 'weekly',
          dtstart: new Date(currentDate).toISOString(),
          interval: 1,
        },
      })
      break

    case 4:
      events.push({
        start: new Date(currentDate).toISOString(),
        end: new Date(currentDate).toISOString(),
        date: currentDate,
        title: title,
        eventId: id,
        rrule: {
          freq: 'monthly',
          dtstart: new Date(currentDate).toISOString(),
          interval: 1,
        },
      })
      break

    default:
      break
  }

  return events
}

const closeReminderDialog = () => {
  addReminderDialog.value.close()
}
const closeDelReminderDialog = () => {
  deleteReminderDialog.value.close()
}
</script>

<template>
  <div class="overflow-auto scroll-hidden p-2">
    <FullCalendar :options="calendarOptions" />

    <dialog ref="addReminderDialog" id="addReminderDialog" class="modal">
      <div class="modal-box">
        <h3 class="text-lg font-bold flex flex-row justify-between">
          <span>Add reminder</span>
          <svg-icon
            type="mdi"
            :path="mdiClose"
            class="cursor-pointer"
            @click="closeReminderDialog"
          ></svg-icon>
        </h3>

        <div class="mt-2">
          <select
            v-model="model"
            class="select select-bordered border-pcs-border w-full max-w-xs mb-2"
          >
            <option
              v-for="model in models"
              :value="model.id"
              :disabled="model.id == 0"
              :selected="model.id == 0"
            >
              {{ model.title }}
            </option>
          </select>
          <textarea
            v-model="reminder.title"
            class="textarea w-full border border-solid border-pcs-border"
            placeholder="Enter the things to be reminded of"
          />
        </div>

        <div class="modal-action">
          <button
            :disabled="!model"
            class="btn bg-pcs-primary text-white"
            @click="addReminderSave"
          >
            save
          </button>
        </div>
      </div>
    </dialog>

    <dialog ref="deleteReminderDialog" id="deleteReminderDialog" class="modal">
      <div class="modal-box">
        <h3 class="text-lg font-bold flex flex-row justify-between">
          <span>Delete reminder</span>
          <svg-icon
            type="mdi"
            :path="mdiClose"
            class="cursor-pointer"
            @click="closeDelReminderDialog"
          ></svg-icon>
        </h3>

        <p>
          This operation will delete all recurring events of this event. Do you
          confirm the deletion?
        </p>

        <div class="modal-action">
          <button class="btn bg-error text-white" @click="deleteReminder">
            delete
          </button>
        </div>
      </div>
    </dialog>
  </div>
</template>

<style scoped></style>
