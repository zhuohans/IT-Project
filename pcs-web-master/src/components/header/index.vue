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
  <header class="w-full flex flex-row bg-white text-pcs-text-primary pt-2 pb-2">
    <div class="w-full flex flex-row justify-between">
      <div class="flex flex-row lg:pl-10 w-[300px]">
        <label
          v-if="!route.fullPath.includes('/user')"
          class="input w-full text-black input-bordered border border-solid input-sm flex justify-between items-center gap-2"
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
          class="mr-2 relative flex items-center text-pcs-primary cursor-pointer"
          @click="toNotice"
        >
          <p
            v-if="events?.length != 0"
            class="bg-red-500 rounded-full absolute -top-1 -right-1"
            style="width: 10px; height: 10px"
          ></p>
          <svg-icon type="mdi" :path="mdiBellOutline"></svg-icon>
        </label>

        <button
          class="btn btn-xs mr-2 bg-pcs-primary text-white"
          @click="logOut"
        >
          log out
        </button>
      </div>

      <div v-else class="flex flex-row gap-x-2 mr-2">
        <span
          class="p-1 cursor-pointer bg-pcs-primary rounded pl-2 pr-2"
          @click="toLogin"
        >
          login
        </span>
        <span
          @click="toRegisterPage"
          class="p-1 cursor-pointer bg-pcs-primary rounded pl-2 pr-2"
        >
          register
        </span>
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
