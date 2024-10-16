<script setup lang="ts">
import { mdiHome, mdiMagnify } from '@mdi/js'
//@ts-ignore
import logo from '@/assets/img/logo.png'
//@ts-ignore
import noneHeader from '@/assets/img/NoneHeadImg.png'
import { useRouter, useRoute } from 'vue-router'
import { inject, provide, ref, watch } from 'vue'
import lodash from 'lodash'
import * as homeApi from '@/api/home'
import { deleteCookie } from '@/utils/CommonUtil'
import { mdiBellOutline } from '@mdi/js'
import EventNotice from '@/pages/user/notice/index.vue'
import * as userApi from '@/api/user'

const router = useRouter()
const route = useRoute()
const userInfo = inject('userInfo')
const isSpecies = inject('isSpecies')
const events = inject('events')

const userClick = () => {
  router.push('/user')
}

const homeClick = () => {
  if (route.fullPath == '/home') {
    router.go(0)
  } else {
    router.push('/home')
  }
}

const toRegisterPage = () => {
  router.push('/register')
}
const toLogin = () => {
  router.push('/login')
}

let speciesList = inject('speciesList', [])
let searchSpecies = inject('searchSpecies', [])
const commonFamily = inject('commonFamily')
const popuCla = inject('popuCla')

const debouncedGetClassificationList = lodash.debounce((newValue) => {
  if (searchInputFocus.value) {
    for (let it of commonFamily.value) {
      it.active = false
    }
    commonFamily.value[0].active = true
    for (let it of popuCla.value) {
      it.active = false
    }
    popuCla.value[0].active = true

    searchSpecies.value = newValue
    homeApi
      .getLastSpecies({
        page: { current: 1, size: 99999 },
        latinNameLike: newValue,
      })
      .then((res) => {
        speciesList.value = res.records
      })
  }
}, 300)

const searchInput = ref()
const searchInputFocus = ref()
const focusChange = () => {
  searchInputFocus.value = true
}
const blurChange = () => {
  searchInputFocus.value = false
}
watch(searchSpecies, (newValue, OldValue, onCleanup) => {
  isSpecies.value = false
  debouncedGetClassificationList(newValue)
})

const logOut = () => {
  localStorage.removeItem('yzxToken')
  deleteCookie('yzxToken')
  if (route.fullPath == '/home') {
    router.go(0)
  } else {
    router.push('/home').then(() => {
      router.go(0)
    })
  }
}

const toNotice = () => {}
</script>

<template>
  <header
    class="w-full flex flex-row justify-between bg-pcs-primary text-pcs-text-primary pt-2 pb-2"
  >
    <div class="flex flex-row items-center">
      <span class="text-center">
        <img :src="logo" class="h-10 ml-4" alt="LOGO" />
      </span>
    </div>

    <div class="w-full flex flex-row justify-center">
      <div class="md: w-56 lg:container flex flex-row justify-between">
        <div class="flex flex-row lg:-ml-14 items-center">
          <svg-icon
            @click="homeClick"
            class="mr-4 cursor-pointer"
            type="mdi"
            :path="mdiHome"
            :size="40"
          ></svg-icon>

          <label
            v-if="!route.fullPath.includes('/user')"
            class="input text-black input-bordered border border-solid input-sm flex items-center gap-2"
          >
            <input
              @focus="focusChange"
              @blur="blurChange"
              ref="searchInput"
              type="text"
              v-model="searchSpecies"
              placeholder="Search species"
            />
            <svg-icon
              type="mdi"
              :path="mdiMagnify"
              class="text-pcs-primary"
            ></svg-icon>
          </label>
        </div>

        <div v-if="userInfo != null" class="flex flex-row items-center">
          <label
            for="notice-drawer"
            class="mr-2 relative flex items-center cursor-pointer"
            @click="toNotice"
          >
            <p
              v-if="events?.length != 0"
              class="bg-red-500 rounded-full absolute -top-1 -right-1"
              style="width: 10px; height: 10px"
            ></p>
            <svg-icon type="mdi" :path="mdiBellOutline"></svg-icon>
          </label>

          <div
            class="flex flex-row items-center gap-x-2 mr-4 cursor-pointer"
            @click="userClick"
          >
            <img
              :src="userInfo?.avatar ? '/img/' + userInfo?.avatar : noneHeader"
              width="30"
              height="30"
              alt="用户头像"
              class="rounded-full"
            />
            <span>{{ userInfo?.username }}</span>
          </div>

          <button class="btn btn-xs mr-2" @click="logOut">log out</button>
        </div>

        <div v-else class="flex flex-row gap-x-2">
          <span class="p-1 cursor-pointer" @click="toLogin">login</span>
          <span @click="toRegisterPage" class="p-1 cursor-pointer">
            register
          </span>
        </div>
      </div>
    </div>
  </header>
  <div class="drawer drawer-end z-50">
    <input id="notice-drawer" type="checkbox" class="drawer-toggle" />
    <div class="drawer-side">
      <label
        for="notice-drawer"
        aria-label="close sidebar"
        class="drawer-overlay"
      ></label>
      <event-notice class="w-1/3 h-screen bg-pcs-species" />
    </div>
  </div>
</template>

<style scoped></style>
