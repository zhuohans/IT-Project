<script setup lang="ts">
import * as userApi from '@/api/user/index'
import { inject, ref } from 'vue'
//@ts-ignore
import speciesImg from '@/assets/img/species.png'
import * as homeApi from '@/api/home'
import { mdiArrowLeft, mdiStar, mdiWindowClose } from '@mdi/js'
import lodash from 'lodash'
import { showAlert } from '@/utils/ComponentRegister'
import { onBeforeRouteLeave, useRouter } from 'vue-router'
import SpeciesDetail from '@/components/species_detail/index.vue'

const router = useRouter()
const userInfo = inject('userInfo')
const albumPage = ref()
const current = ref(1)
const isWheel = ref(false)
const initData = () => {
  userApi.album({ current: current.value, size: 40 }).then((res) => {
    if (current.value > albumPage.value?.pages) {
      showAlert("It's already the last page.", 'info')
      return
    }
    if (current.value == 1) {
      albumPage.value = res
    } else {
      albumPage.value.records = albumPage.value.records.concat(res.records)
    }
  })
}
initData()

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
  isView.value = false
  router.go(-1)
}

const onWheel = () => {
  isWheel.value = true
}
// 创建一个防抖函数
const debouncedGetClassificationList = lodash.debounce((newValue) => {
  current.value++
  initData()
}, 300)

const onScroll = () => {
  const scrollContainer = document.getElementById('albumContainer')
  const scrollTop = scrollContainer.scrollTop
  const scrollHeight = scrollContainer.scrollHeight
  const clientHeight = scrollContainer.clientHeight

  if (isWheel.value) {
    if (scrollTop + clientHeight >= scrollHeight - 10) {
      debouncedGetClassificationList()
    }
  }
}
onBeforeRouteLeave(() => {
  current.value = 1
})

const speciesDialogInfo = ref()
const closeModal = () => {
  speciesDialog.value.close()
  speciesDialogInfo.value.isEdite = false
}
</script>

<template>
  <div
    class="overflow-auto scroll-hidden"
    id="albumContainer"
    @wheel="onWheel"
    @scroll="onScroll"
    style="height: 43rem"
  >
    <div v-if="!isView" class="grid grid-cols-4 gap-2">
      <div
        v-for="item in albumPage?.records"
        @click="viewSpecies(item.id)"
        class="cursor-pointer card shadow rounded-xl"
      >
        <div class="relative">
          <svg-icon
            class="absolute bottom-2 right-2 text-red-600"
            type="mdi"
            :path="mdiStar"
            v-if="!(item.createBy == userInfo.userId)"
          ></svg-icon>
          <img
            class="w-full h-80"
            :src="item.imgPath ? '/img/' + item.imgPath : speciesImg"
            alt=""
          />
        </div>

        <p class="flex justify-center font-bold text-pcs-primary pt-1 pb-1">
          {{ item.latinName }}
        </p>
      </div>
    </div>

    <dialog ref="speciesDialog" id="speciesDialog" class="modal">
      <div
        class="modal-box min-w-[1200px] h-[800px] overflow-x-auto overflow-y-hidden"
      >
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
