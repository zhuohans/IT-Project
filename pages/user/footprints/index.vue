<script setup lang="ts">
import { inject, ref } from 'vue'
import {
  mdiThumbUp,
  mdiStar,
  mdiMenu,
  mdiEyeOutline,
  mdiCommentProcessingOutline,
  mdiArrowLeft,
  mdiWindowClose,
} from '@mdi/js'
import * as userApi from '@/api/user'
//@ts-ignore
import noneHeadImg from '@/assets/img/NoneHeadImg.png'
import * as homeApi from '@/api/home'
import lodash from 'lodash'
import { showAlert } from '@/utils/ComponentRegister'
import { onBeforeRouteLeave, useRouter } from 'vue-router'
import SpeciesDetail from '@/components/species_detail/index.vue'

const router = useRouter()
const userInfo = inject('userInfo')
const isWheel = ref(false)
const userMenu = ref([
  { id: 0, title: 'all', icon: mdiMenu, active: true },
  { id: 1, title: 'view', icon: mdiEyeOutline, active: false },
  { id: 2, title: 'like', icon: mdiThumbUp, active: false },
  { id: 3, title: 'collect', icon: mdiStar, active: false },
  { id: 4, title: 'comment', icon: mdiCommentProcessingOutline, active: false },
])

const actClick = (item) => {
  current.value = 1
  for (let item of userMenu.value) {
    item.active = false
  }
  item.active = true
  currentType.value = item.id
  getFootprints(item.id)
}

const current = ref(1)
const size = ref(20)

const footprints = ref()
const currentType = ref(0)
const getFootprints = (type) => {
  if (current.value > footprints.value?.pages) {
    showAlert("It's already the last page.", 'info')
    return
  }
  userApi
    .footprints({ current: current.value, size: 20 }, currentType.value)
    .then((res) => {
      if (current.value == 1) {
        footprints.value = res
      } else {
        footprints.value.records = footprints.value.records.concat(res.records)
      }
    })
}
getFootprints(0)

const speciesDialog = ref()
const speciesInfo = inject('speciesInfo')
const isView = ref(false)
const viewSpecies = (id) => {
  current.value = 1
  isWheel.value = false

  homeApi.viewSpecies(id).then((res) => {
    speciesInfo.value = res
    speciesDialog.value.showModal()
  })
}

const back = () => {
  speciesInfo.value = ''
  isView.value = false
  router.go(-1)
}

onBeforeRouteLeave(() => {
  current.value = 1
})

// 创建一个防抖函数
const debouncedGetClassificationList = lodash.debounce((newValue) => {
  current.value++
  getFootprints(currentType.value)
}, 300)

const onScroll = () => {
  const scrollContainer = document.getElementById('footprintsContainer')
  const scrollTop = scrollContainer.scrollTop
  const scrollHeight = scrollContainer.scrollHeight
  const clientHeight = scrollContainer.clientHeight

  if (isWheel.value) {
    if (scrollTop + clientHeight >= scrollHeight - 10) {
      debouncedGetClassificationList()
    }
  }
}

const onWheel = () => {
  isWheel.value = true
}

const speciesDialogInfo = ref()
const closeModal = () => {
  speciesDialog.value.close()
  speciesDialogInfo.value.isEdite = false
}
</script>

<template>
  <div
    class="overflow-auto scroll-hidden"
    id="footprintsContainer"
    @wheel="onWheel"
    style="height: 43rem"
    @scroll="onScroll"
  >
    <div v-if="!isView" class="flex flex-row">
      <p
        v-for="item in userMenu"
        @click="actClick(item)"
        class="flex flex-row p-2 rounded ml-4 cursor-pointer gap-x-1"
        :class="{ 'bg-pcs-primary text-white': item.active }"
      >
        <svg-icon type="mdi" :path="item.icon"></svg-icon>
        {{ item.title }}
      </p>
    </div>

    <div v-if="!isView">
      <div
        v-for="item in footprints?.records"
        class="pt-2 border-b border-solid border-pcs-border pb-2 flex flex-row mt-2"
      >
        <img
          class="w-10 h-10 rounded-full mr-1 ml-2"
          :src="userInfo?.avatar ? '/img/' + userInfo?.avatar : noneHeadImg"
          alt=""
        />
        <div class="flex flex-col ml-1 gap-y-1">
          <p>
            <span class="font-bold text-pcs-primary mr-3">
              {{ userInfo?.username }}
            </span>
            <span class="text-gray-400">&nbsp;{{ item.actTime }}</span>
          </p>
          <p v-if="item.act == 1" style="letter-spacing: 1px">
            Browsed through
            <span class="text-pcs-primary">{{ item.speciesUserName }}</span>
            's works,
            <span
              class="text-pcs-primary cursor-pointer"
              @click="viewSpecies(item.speciesId)"
            >
              {{ item.latinName }}
            </span>
          </p>
          <p v-if="item.act == 2" style="letter-spacing: 1px">
            Liked the work of
            <span class="text-pcs-primary">{{ item.speciesUserName }}</span>
            ,
            <span
              class="text-pcs-primary cursor-pointer"
              @click="viewSpecies(item.speciesId)"
            >
              {{ item.latinName }}
            </span>
          </p>
          <p v-if="item.act == 3" style="letter-spacing: 1px">
            Collected the works of
            <span class="text-pcs-primary">{{ item.speciesUserName }}</span>
            ,
            <span
              class="text-pcs-primary cursor-pointer"
              @click="viewSpecies(item.speciesId)"
            >
              {{ item.latinName }}
            </span>
          </p>
          <p v-if="item.act == 4" style="letter-spacing: 1px">
            Commented on
            <span class="text-pcs-primary">{{ item.speciesUserName }}</span>
            's work,
            <span
              class="text-pcs-primary cursor-pointer"
              @click="viewSpecies(item.speciesId)"
            >
              {{ item.latinName }}
            </span>
          </p>
        </div>
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
  </div>
</template>

<style scoped></style>
