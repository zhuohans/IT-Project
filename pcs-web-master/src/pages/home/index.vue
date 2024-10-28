<script setup lang="ts">
import * as homeApi from '@/api/home/index'
import { inject, provide, ref } from 'vue'
//@ts-ignore
import noneImg from '@/assets/img/NoneImg.png'
import { showAlert } from '@/utils/ComponentRegister'
import { onBeforeRouteLeave } from 'vue-router'
import lodash from 'lodash'
import router from '@/router'
import { mdiWindowClose } from '@mdi/js'
import SpeciesDetail from '@/components/species_detail/index.vue'

const species = inject('species')
let speciesList = inject('speciesList')
const isSpecies = inject('isSpecies')
const speciesInfo = inject('speciesInfo')
const commonFamily = inject('commonFamily')
const popuCla = inject('popuCla')

const initData = () => {
  current.value = 1
  species.value = ''
  speciesList.value = []
  isSpecies.value = false
  speciesInfo.value = {}
}

const current = ref(1)
const isWheel = ref(false)

const lastPages = inject('pageSize')
const popularClassification = ref()
const seasonClassification = ref()
const currentSelect = inject('currentSelect')
const getSpeciesList = () => {
  isWheel.value = false
  if (current.value > lastPages.value) {
    showAlert("It's already the last page.", 'info')
    return
  }
  homeApi
    .getLastSpecies({
      page: {
        current: current.value,
        size: 40,
      },
      popularClassification: currentSelect.value[1],
      seasonClassification:
        currentSelect.value[0] == -1 ? null : currentSelect.value[0],
    })
    .then((res) => {
      lastPages.value = res.pages
      if (current.value == 1) {
        speciesList.value = res.records
      } else {
        speciesList.value = speciesList.value.concat(res.records)
      }
    })
}

getSpeciesList()

const currentItem = ref()
const speciesDialog = ref()
const speciesClick = (item: any) => {
  current.value = 1
  if (!item.imgPath) {
    showAlert('Currently no image, waiting for you to upload.', 'info')
    return
  }
  homeApi.viewSpecies(item.id).then((res) => {
    speciesInfo.value = res
    speciesDialog.value.showModal()
  })
}
const speciesDialogInfo = ref()
const closeModal = () => {
  speciesDialog.value.close()
  speciesDialogInfo.value.isEdite = false
}

let searchSpecies = inject('searchSpecies')

onBeforeRouteLeave(() => {
  initData()
})

// 创建一个防抖函数
const debouncedGetClassificationList = lodash.debounce((newValue) => {
  if (searchSpecies.value) {
    // showAlert("It's already the last page.", 'info')
    return
  }
  current.value++
  if (!currentItem.value || currentItem.value.id == '-1') {
    seasonClassification.value = null
    popularClassification.value = null
    getSpeciesList()
  }

  if ('123'.includes(currentItem.value.id)) {
    seasonClassification.value = null
    popularClassification.value = currentItem.value.type
    getSpeciesList()
  } else {
    popularClassification.value = null
    seasonClassification.value = currentItem.value.type
    getSpeciesList()
  }
}, 300)

const onWheel = () => {
  isWheel.value = true
}
const onScroll = () => {
  const scrollContainer = document.getElementById('species-container')
  const scrollTop = scrollContainer.scrollTop
  const scrollHeight = scrollContainer.scrollHeight
  const clientHeight = scrollContainer.clientHeight

  // 仅当scrollTop发生变化时执行逻辑
  if (isWheel.value) {
    if (scrollTop + clientHeight >= scrollHeight - 10) {
      debouncedGetClassificationList()
    }
  }
}

const userMenuClick = (item) => {
  current.value = 1
  isSpecies.value = false
  lastPages.value = 9999
  searchSpecies.value = ''
  for (let it of commonFamily.value) {
    it.active = false
  }
  item.active = true

  currentSelect.value[0] = item.type
  for (let it of popuCla.value) {
    it.active = false
  }
  popuCla.value[0].active = true
  currentSelect.value[1] = popuCla.value[0].type
  getSpeciesList()
}

const popuCLaClick = (item) => {
  current.value = 1
  isSpecies.value = false
  if (searchSpecies.value) {
    searchSpecies.value = ''
    currentSelect.value[0] = null
  }
  lastPages.value = 9999
  for (let it of popuCla.value) {
    it.active = false
  }
  item.active = true
  currentSelect.value[1] = item.type
  getSpeciesList()
}
</script>

<template>
  <main class="h-full">
    <div
      class="flex flex-row justify-center gap-x-2 mt-2 mb-2 bg-white pt-2 pb-2"
    >
      <span
        v-for="item in popuCla"
        @click="popuCLaClick(item)"
        class="border border-solid border-pcs-border pl-2 pr-2 pb-1 pt-1 rounded cursor-pointer"
        :class="item.active ? 'bg-pcs-primary text-white' : ''"
      >
        {{ item.title }}
      </span>
    </div>

    <div role="tablist" class="tabs tabs-bordered max-w-[800px] overflow-auto">
      <a
        role="tab"
        class="tab"
        v-for="item in commonFamily"
        @click="userMenuClick(item)"
        :class="item.active ? 'tab-active text-pcs-primary' : ''"
      >
        <svg-icon type="mdi" :path="item.icon" class="mr-1"></svg-icon>
        {{ item.title }}
      </a>
    </div>

    <div
      @wheel="onWheel"
      @scroll="onScroll"
      id="species-container"
      class="grid scroll-hidden overflow-auto pl-4 pr-4"
      style="height: 50rem"
      :class="{
        'mt-2 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4':
          !isSpecies,
        'flex flex-col': isSpecies,
      }"
    >
      <div v-if="isSpecies" class="flex flex-col" style="height: 43rem">
        <router-view />
      </div>

      <div
        v-else
        v-for="item in speciesList"
        :key="item.name"
        @click="speciesClick(item)"
        style="height: fit-content"
        :class="{
          'card shadow mb-4 break-inside-avoid cursor-pointer': !isSpecies,
        }"
      >
        <div>
          <div class="relative">
            <img
              :src="item.imgPath ? '/img' + item.imgPath : noneImg"
              :alt="item.latinName"
              class="w-full h-44 object-cover rounded-xl"
            />
            <span
              v-if="!item.imgPath"
              class="absolute top-2/3 left-1/4 text-white font-bold"
            >
              No image available.
            </span>
          </div>

          <div class="p-2 text-center">{{ item.latinName }}</div>
        </div>
      </div>

      <div v-if="speciesList?.length == 0">
        <p class="text-pcs-primary font-bold text-2xl">No data available.</p>
      </div>
    </div>

    <dialog ref="speciesDialog" id="speciesDialog" class="modal">
      <div class="modal-box min-w-[1200px] h-[800px] overflow-hidden">
        <p class="flex justify-end">
          <svg-icon
            @click="closeModal"
            type="mdi"
            class="cursor-pointer"
            :path="mdiWindowClose"
          ></svg-icon>
        </p>
        <species-detail ref="speciesDialogInfo" />
      </div>
    </dialog>
  </main>
</template>

<style></style>
