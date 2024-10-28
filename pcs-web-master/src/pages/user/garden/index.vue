<script setup lang="ts">
import * as userApi from '@/api/user/index'
import { ref } from 'vue'
// @ts-ignore
// import gardenBg from '@/assets/img/garden_bg.jpg'
import gardenBg from '@/assets/img/GardenBg2.png'
// @ts-ignore
import noneImg from '@/assets/img/NoneImg.png'
import { mdiPlus, mdiClose } from '@mdi/js'
import lodash from 'lodash'
import { showAlert } from '@/utils/ComponentRegister'

const collectList = ref([])
const current = ref(1)

const initData = () => {
  if (current.value > collectList.value?.pages) {
    showAlert("It's already the last page.", 'info')
    return
  }
  userApi.album({ current: current.value, size: 20 }).then((res) => {
    if (current.value == 1) {
      collectList.value = res.records
    } else {
      collectList.value = collectList.value.concat(res.records)
    }
  })
}
initData()

const flowerSpots = ref(
  Array(18).fill({
    id: 0,
    imgPath: noneImg,
    latinName: 'Drag the object species here',
  }),
)

let draggedItem = ref(null)
const gardenSpeciesList = ref([])

userApi.getGardenSpeciesList().then((res) => {
  gardenSpeciesList.value = res
  console.log(gardenSpeciesList.value)
  if (gardenSpeciesList.value.length !== 0) {
    gardenSpeciesList.value.forEach((item) => {
      let sort = item.sort
      flowerSpots.value[sort] = item
    })
  }
})

const dragStart = (item) => {
  draggedItem.value = item
}

const dropFlower = (index) => {
  const pre = flowerSpots.value[index]
  if (flowerSpots.value[index].id != -1) {
    flowerSpots.value[index] = draggedItem.value
    draggedItem.value = null
    if (pre.id == 0) {
      userApi.addToGarden({
        speciesId: flowerSpots.value[index].id,
        sort: index,
        imgPath: flowerSpots.value[index].imgPath,
        latinName: flowerSpots.value[index].latinName,
        cnName: flowerSpots.value[index].cnName,
      })
    } else {
      console.log(flowerSpots.value[index], 'after')
      userApi.updateToGarden({
        id: pre.id,
        speciesId: flowerSpots.value[index].id,
        sort: index,
        imgPath: flowerSpots.value[index].imgPath,
        latinName: flowerSpots.value[index].latinName,
        cnName: flowerSpots.value[index].cnName,
      })
    }
  }
}

const addSpecies = () => {
  if (flowerSpots.value.length <= 1) {
    flowerSpots.value.unshift({
      id: 0,
      imgPath: noneImg,
      latinName: 'Drag the object species here',
    })
  } else {
    const length = flowerSpots.value.length
    flowerSpots.value.splice(length - 1, 0, {
      id: 0,
      imgPath: noneImg,
      latinName: 'Drag the object species here',
    })
  }
}

const menuVisible = ref(false)
const menuX = ref(0)
const menuY = ref(0)
const currentClickItem = ref({ index: 0, spot: null })

const showMenu = (event, spot, index) => {

  menuVisible.value = true
  menuX.value = event.pageX
  menuY.value = event.pageY
  currentClickItem.value.index = index
  currentClickItem.value.spot = spot
}
const removeSpecies = (spot, index) => {

  userApi.deleteGardenSpecies(spot).then((res) => {
    flowerSpots.value[index] = {
      id: 0,
      imgPath: noneImg,
      latinName: 'Drag the object species here',
    }
  })
}


const debouncedGetClassificationList = lodash.debounce((newValue) => {
  current.value++
  initData()
}, 300)

const onScroll = () => {
  const scrollContainer = document.getElementById('albumContainer')
  const scrollTop = scrollContainer.scrollTop
  const scrollHeight = scrollContainer.scrollHeight
  const clientHeight = scrollContainer.clientHeight

  if (scrollTop + clientHeight >= scrollHeight - 10) {
    debouncedGetClassificationList()
  }
}
</script>

<template>
  <div class="flex flex-row" style="height: 43rem">
    <div
      class="h-full w-1/6 overflow-auto scroll-hidden"
      id="albumContainer"
      @scroll="onScroll"
    >
      <p
        class="z-10 sticky top-0 text-white font-bold text-center pt-2 pb-2 rounded-t-xl bg-pcs-primary"
      >
        album
      </p>

      <ul class="bg-pcs-species">
        <li
          v-if="collectList.length > 0"
          v-for="(item, index) in collectList"
          :key="index"
          class="mb-2 card shadow"
          draggable="true"
          @dragstart="dragStart(item)"
        >
          <img
            :src="'/img' + item.imgPath"
            class="h-32"
            :alt="item.latinName"
          />
          <p class="pt-2 pb-2 text-center bg-white text-pcs-primary">
            {{ item.latinName }}
          </p>
        </li>
        <li v-else class="text-pcs-primary font-bold text-2xl">No data available.</li>
      </ul>
    </div>

    <div
      class="overflow-auto scroll-hidden w-5/6 bg-pcs-species grid grid-rows-3 grid-cols-6 gap-x-8 ml-4 grid-flow-row"
    >
      <div
        class="relative ml-2 mr-2 mt-16"
        v-for="(spot, index) in flowerSpots"
        :key="index"
        @dragover.prevent
        @drop="dropFlower(index)"
      >
        <svg-icon
          v-if="spot.id != 0"
          @click="removeSpecies(spot, index)"
          class="absolute cursor-pointer -top-14 text-error right-6 z-20"
          type="mdi"
          :path="mdiClose"
        ></svg-icon>

        <div
          class="absolute w-32 h-32 rounded-full -top-14 left-4 z-10 bg-white overflow-hidden"
        >
          <img
            v-if="spot.id == 0"
            :src="noneImg"
            class="w-full h-full object-cover"
            alt=""
          />
          <img
            v-else
            :src="'/img/' + spot.imgPath"
            class="w-full h-full object-cover"
            alt=""
          />
        </div>

        <div class="absolute top-20">
          <img class="absolute left-6 -top-14" :src="gardenBg" alt="" />
          <div
            class="bg-gray-500 shadow-xl shadow-gray-800 h-14 flex flex-row justify-center items-center text-white"
          >
            <span class="whitespace-nowrap text-center text-xs pl-1 pr-1 w-44">
              {{ spot ? spot.latinName : '' }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.garden-h {
  height: 40rem;
}
</style>
