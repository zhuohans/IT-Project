<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import MyHeader from '@/components/header/index.vue'
import { onMounted, provide, ref } from 'vue'
import { UserPo } from '@/api/user/type'
import * as userApi from '@/api/user'
import { deleteCookie } from '@/utils/CommonUtil'
import { useRouter } from 'vue-router'
import {
  mdiFlower,
  mdiWeatherSunny,
  mdiFoodApple,
  mdiSnowflake,
  mdiTallyMark4,
  mdiAccount,
  mdiFootPrint,
  mdiImageAlbum,
  mdiCalendarMonthOutline,
  mdiFlowerOutline,
  mdiSquareEditOutline,
  mdiPlus,
  mdiWindowClose,
} from '@mdi/js'
import logo from '@/assets/img/logo.png'
import { mdiHome, mdiMagnify } from '@mdi/js'
const router = useRouter()
let speciesList = ref()
provide('speciesList', speciesList)
const isSpecies = ref(false)
provide('isSpecies', isSpecies)
const family = ref('')
const genus = ref('')
const species = ref('')
const speciesInfo = ref()
provide('family', family)
provide('genus', genus)
provide('species', species)
provide('speciesInfo', speciesInfo)
const searchSpecies = ref('')
provide('searchSpecies', searchSpecies)
const showCurrentLocation = ref(false)
provide('showCurrentLocation', showCurrentLocation)

const commonFamily = ref([
  {
    id: '-1',
    active: true,
    type: -1,
    value: -1,
    icon: mdiTallyMark4,
    title: 'Latest publication',
    label: 'Latest publication',
    children: [],
  },

  {
    id: '4',
    active: false,
    type: 0,
    value: 0,
    title: 'Spring',
    label: 'Spring',
    icon: mdiFlower,
    children: [
      {
        id: '1',
        active: false,
        type: 0,
        value: 0,
        title: 'Flower',
        label: 'Flower',
        children: [],
      },
      {
        id: '2',
        active: false,
        type: 1,
        value: 1,
        title: 'Vegetable',
        label: 'Vegetable',
        children: [],
      },
      {
        id: '3',
        active: false,
        type: 2,
        value: 2,
        title: 'Tree',
        label: 'Tree',
        children: [],
      },
    ],
  },
  {
    id: '6',
    active: false,
    type: 1,
    value: 1,
    title: 'Summer',
    label: 'Summer',
    icon: mdiWeatherSunny,
    children: [
      {
        id: '1',
        active: false,
        type: 0,
        value: 0,
        title: 'Flower',
        label: 'Flower',
        children: [],
      },
      {
        id: '2',
        active: false,
        type: 1,
        value: 1,
        title: 'Vegetable',
        label: 'Vegetable',
        children: [],
      },
      {
        id: '3',
        active: false,
        type: 2,
        value: 2,
        title: 'Tree',
        label: 'Tree',
        children: [],
      },
    ],
  },
  {
    id: '7',
    active: false,
    type: 2,
    value: 2,
    title: 'Autumn',
    label: 'Autumn',
    icon: mdiFoodApple,
    children: [
      {
        id: '1',
        active: false,
        type: 0,
        value: 0,
        title: 'Flower',
        label: 'Flower',
        children: [],
      },
      {
        id: '2',
        active: false,
        type: 1,
        value: 1,
        title: 'Vegetable',
        label: 'Vegetable',
        children: [],
      },
      {
        id: '3',
        active: false,
        type: 2,
        value: 2,
        title: 'Tree',
        label: 'Tree',
        children: [],
      },
    ],
  },
  {
    id: '8',
    active: false,
    type: 3,
    value: 3,
    title: 'Winter',
    label: 'Winter',
    icon: mdiSnowflake,
    children: [
      {
        id: '1',
        active: false,
        type: 0,
        value: 0,
        title: 'Flower',
        label: 'Flower',
        children: [],
      },
      {
        id: '2',
        active: false,
        type: 1,
        value: 1,
        title: 'Vegetable',
        label: 'Vegetable',
        children: [],
      },
      {
        id: '3',
        active: false,
        type: 2,
        value: 2,
        title: 'Tree',
        label: 'Tree',
        children: [],
      },
    ],
  },
])
const popuCla = ref([
  {
    id: '-1',
    active: true,
    type: null,
    value: -1,
    title: 'All',
    label: 'All',
    children: [],
  },
  {
    id: '1',
    active: false,
    type: 0,
    value: 0,
    title: 'Flower',
    label: 'Flower',
    children: [],
  },
  {
    id: '2',
    active: false,
    type: 1,
    value: 1,
    title: 'Vegetable',
    label: 'Vegetable',
    children: [],
  },
  {
    id: '3',
    active: false,
    type: 2,
    value: 2,
    title: 'Tree',
    label: 'Tree',
    children: [],
  },
])
const currentSelect = ref([-1])
const pageSize = ref(9999)
provide('commonFamily', commonFamily)
provide('currentSelect', currentSelect)
provide('pageSize', pageSize)
provide('popuCla', popuCla)

let userInfo = ref(new UserPo())
userApi.getUserInfo().then((res: any) => {
  userInfo.value = res
  if (!res) {
    localStorage.removeItem('yzxToken')
    deleteCookie('yzxToken')
  }
})
provide('userInfo', userInfo)

const events = ref([])

const getEventList = async () => {
  events.value = await userApi.getEventList()
}
getEventList()
setInterval(() => {
  getEventList()
}, 60000)
provide('events', events)

const route = useRoute()
const homeClick = () => {
  if (route.fullPath == '/home') {
    router.go(0)
  } else {
    router.push('/home')
  }
}
const slideMenu = ref([
  {
    id: '1',
    active: false,
    icon: mdiHome,
    title: 'home',
    path: '/home',
    children: [],
  },
  {
    id: '2',
    active: false,
    icon: mdiAccount,
    title: 'profile',
    path: '/user/footprints',
    children: [],
  },
  {
    id: '5',
    active: false,
    icon: mdiCalendarMonthOutline,
    title: 'calendar',
    path: '/user/calendar',
    children: [],
  },
])

onMounted(() => {
  for (let item of slideMenu.value) {
    item.active = route.fullPath == item.path
  }
})

const menuClick = (item) => {
  for (let it of slideMenu.value) {
    it.active = false
  }
  item.active = true
  router.push(item.path)
}
</script>

<template>
  <div
    class="h-screen flex flex-row"
    style="background-color: rgb(244, 244, 244)"
  >
    <ul class="bg-pcs-primary w-64 text-white">
      <li class="flex justify-center mt-2 pr-2">
        <img :src="logo" class="h-10" alt="LOGO" />
      </li>
      <li
        class="flex justify-center mt-2 items-center pr-2 pt-1 pb-1 cursor-pointer rounded"
        v-for="item in slideMenu"
        v-show="item.id == '1' || userInfo != null"
        :class="item.active ? 'bg-pcs-tab-active' : ''"
        @click="menuClick(item)"
      >
        <svg-icon
          @click="homeClick"
          class="mr-2 cursor-pointer"
          type="mdi"
          :path="item.icon"
          :size="30"
        ></svg-icon>
        <span class="w-[36px]">{{ item.title }}</span>
      </li>
    </ul>

    <div class="w-full flex flex-col bg-gray-100">
      <my-header class="w-[50px]" />
      <router-view class="mt-2" />
    </div>
  </div>
</template>

<style scoped></style>
