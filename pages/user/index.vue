<script setup lang="ts">
//@ts-ignore
import noneHeadImg from '@/assets/img/NoneHeadImg.png'
//@ts-ignore
import upload from '@/assets/img/upload.png'
import * as userApi from '@/api/user/index'
import { inject, onMounted, provide, ref, watch } from 'vue'
import { UserPo } from '@/api/user/type'
import {
  mdiAccount,
  mdiFootPrint,
  mdiImageAlbum,
  mdiCalendarMonthOutline,
  mdiFlowerOutline,
  mdiSquareEditOutline,
  mdiPlus,
  mdiWindowClose,
} from '@mdi/js'
import { useRouter, useRoute } from 'vue-router'
//@ts-ignore
import speciesImg from '@/assets/img/species.png'
import * as homeApi from '@/api/home/index'
import { getClassificationList } from '@/api/home/index'
import lodash from 'lodash'
import { showAlert } from '@/utils/ComponentRegister'

const router = useRouter()
const route = useRoute()

const userInfo = inject('userInfo')

const userMenu = ref([
  {
    id: '2',
    title: 'footprints',
    number: 1,
    active: false,
    icon: mdiFootPrint,
    path: '/user/footprints',
  },
  {
    id: '3',
    title: 'album',
    number: 1,
    active: false,
    icon: mdiImageAlbum,
    path: '/user/album',
  },
  {
    id: '5',
    title: 'garden',
    number: 1,
    active: false,
    icon: mdiFlowerOutline,
    path: '/user/garden',
  },
])

const tabClick = (item) => {
  for (let item of userMenu.value) {
    item.active = false
  }
  item.active = true
  router.push(item.path)
}

onMounted(() => {
  for (let item of userMenu.value) {
    item.active = item.path == route.fullPath
  }
})

const isEdit = ref(false)
const userInfoEdit = ref()
const editClick = () => {
  userInfoEdit.value.showModal()
}
provide('isEdit', isEdit)

const uploadSpeciesRef = ref()
const uploadClick = () => {
  uploadSpeciesRef.value.showModal()
}

const receiveCnt = ref()
userApi.receiveCnt().then((res) => {
  receiveCnt.value = res
})
const imageUploader = ref()
const clickUploadBtn = () => {
  const fileInput = document.getElementById('fileInput')
  fileInput.click()
}
const uploadSpeciesImgPath = ref()
const uploadFile = ref('')
onMounted(() => {
  document.getElementById('fileInput').addEventListener('change', function (e) {
    const file = e.target.files[0]
    uploadFile.value = file
    if (file) {
      const reader = new FileReader()
      reader.onload = function (e) {
        const imgElement = document.getElementById('uploadSpeciesImg')
        uploadSpeciesImgPath.value = e.target.result
        imgElement.src = uploadSpeciesImgPath.value
      }
      reader.readAsDataURL(file)
    }
  })
})

const showDropMenu = ref(false)
const classificationName = ref()
const classificationList = ref([])
// 创建一个防抖函数
const debouncedGetClassificationList = lodash.debounce((newValue) => {
  classificationName.value = newValue
  homeApi.getClassificationList(newValue).then((res: any) => {
    classificationList.value = res
    showDropMenu.value = true
  })
}, 300)

const speciesInfoForm = ref({
  popularClassification: 0,
  seasonClassification: 0,
})
const optimalTemperature = ref(0)

const fileInput = ref()
const saveSpecies = () => {
  if (!speciesInfoForm.value.latinName) {
    showAlert('Please enter the species name', 'warning')
    return
  }
  const formData = new FormData()
  formData.append('file', uploadFile.value, uploadFile.value.name)
  userApi
    .uploadFile(formData)
    .then((res: any) => {
      speciesInfoForm.value.imgPath = res
      speciesInfoForm.value.optimalTemperature = optimalTemperature.value + '℃'
      userApi.addSpecies(speciesInfoForm.value).then((res) => {
        uploadSpeciesRef.value.close()
        clearForm()
        showAlert('added successfully.', 'info')
        fileInput.value.value = ''
        if (route.fullPath == '/user/album') {
          router.go(0)
        }
      })
    })
    .catch(() => {
      fileInput.value.value = ''
    })
}
const clearForm = () => {
  speciesInfoForm.value = {}
  classificationName.value = ''
  classificationName.value = ''
  showDropMenu.value = false
  uploadSpeciesImgPath.value = ''
  uploadFile.value = ''
  const imgElement = document.getElementById('uploadSpeciesImg')
  imgElement.src = speciesImg
  fileInput.value.value = ''
}
const closeModal = () => {
  clearForm()
  uploadSpeciesRef.value.close()
  userInfoEdit.value.close()
}

// 0:春 1：夏 2：秋 3：冬
const seasonCla = ref([
  { id: 0, title: 'Spring' },
  { id: 1, title: 'Summer' },
  { id: 2, title: 'Autumn' },
  { id: 3, title: 'Winter' },
])
// 通俗分类（2：树 1：菜 0：花）
const popularCla = ref([
  { id: 0, title: 'Flower' },
  { id: 1, title: 'Vegetable' },
  { id: 2, title: 'Tree' },
])
let temp = {}
setTimeout(() => {
  temp = JSON.parse(JSON.stringify(userInfo.value))
}, 1000)
const saveUserInfo = (event) => {
  event.preventDefault()
  if (userInfo.value.username == '') {
    showAlert('The account cannot be set to empty', 'error')
    userInfo.value = temp
    return
  }
  if (userInfo.value.email == '') {
    showAlert('Cannot set the email to empty', 'error')
    userInfo.value = temp
    return
  }
  userApi
    .updateUserInfo(userInfo.value)
    .then(() => {
      userInfoEdit.value.close()
    })
    .catch(() => {
      userInfo.value = temp
    })
}

const avatarInput = ref()
const avatarFile = ref()
const onAvatarChange = (e) => {
  let file = avatarInput.value.files[0]
  if (file) {
    avatarFile.value = file
    let reader = new FileReader()
    reader.onload = (e) => {
      const formData = new FormData()
      formData.append('file', avatarFile.value, avatarFile.value.name)
      userApi.uploadFile(formData).then((res: any) => {
        userInfo.value.avatar = res
        userApi.updateUserInfo(userInfo.value)
        avatarFile.value = ''
      })
    }
    reader.readAsDataURL(file)
  }
}

const uploadAvatar = () => {
  avatarInput.value.click()
}
</script>

<template>
  <main class="h-full w-full">
    <section
      class="shadow flex flex-row justify-center border-b border-solid border-gray-300"
    >
      <div
        class="w-full flex flex-row justify-between bg-white gap-x-2 shadow p-2"
      >
        <img
          width="80"
          class="ml-2"
          :src="!userInfo?.avatar ? noneHeadImg : '/img' + userInfo?.avatar"
          alt="No avatar available."
        />
        <div class="w-full flex flex-row justify-between">
          <div class="flex flex-col justify-center">
            <div class="flex flex-row gap-x-2 text-xl font-bold">
              <span>{{ userInfo.username }}</span>
              <p
                @click="editClick"
                class="cursor-pointer flex flex-row items-center bg-pcs-second pl-2 pr-2 pt-1 pb-1 rounded-xl text-white text-xs"
              >
                <svg-icon
                  :size="16"
                  type="mdi"
                  :path="mdiSquareEditOutline"
                ></svg-icon>
                <span>edit</span>
              </p>
            </div>

            <p class="flex flex-col text-sm">
              <span>
                Receive the number of likes：
                <span class="font-bold">{{ receiveCnt?.likeCnt }}</span>
              </span>
              <span>
                Receive the number of collections：
                <span class="font-bold">{{ receiveCnt?.collectCnt }}</span>
              </span>
              <span>
                Number of views：
                <span class="font-bold">{{ receiveCnt?.viewCnt }}</span>
              </span>
            </p>
          </div>

          <div class="flex items-center mr-4">
            <span
              @click="uploadClick"
              class="flex flex-col cursor-pointer p-2 items-center text-pcs-primary border border-solid rounded-xl border-pcs-second"
            >
              <img width="60" :src="upload" alt="上传" />
              <span class="text-xs">upload species</span>
            </span>
          </div>
        </div>
      </div>
    </section>

    <div class="flex flex-col items-center h-full bg-[#f4f4f4]">
      <div class="w-full h-full" style="background-color: #fff">
        <section class="mt-4 max-w-[500px]">
          <div role="tablist" class="tabs tabs-bordered">
            <a
              role="tab"
              class="tab"
              v-for="item in userMenu"
              @click="tabClick(item)"
              :class="{
                'tab-active': item.active,
                'text-pcs-primary': item.active,
              }"
            >
              <svg-icon type="mdi" :path="item.icon"></svg-icon>
              {{ item.title }}
            </a>
          </div>
        </section>

        <section class="mt-2 h-full pl-2 pr-2">
          <router-view class="h-full" />
        </section>
      </div>
    </div>

    <dialog ref="uploadSpeciesRef" id="uploadSpecies" class="modal">
      <div class="modal-box w-1/2 max-w-5xl">
        <p class="flex justify-end">
          <svg-icon
            @click="closeModal"
            type="mdi"
            class="cursor-pointer"
            :path="mdiWindowClose"
          ></svg-icon>
        </p>
        <h3 class="text-lg font-bold text-pcs-primary">Upload Species</h3>

        <div class="w-full mt-2 flex flex-row justify-center items-center">
          <div
            ref="imageUploader"
            @click="clickUploadBtn"
            class="border border-solid border-pcs-border w-56 rounded-xl flex flex-col justify-center items-center cursor-pointer"
          >
            <img
              id="uploadSpeciesImg"
              class="w-44 h-44"
              :src="speciesImg"
              alt=""
            />
            <p
              v-if="!uploadSpeciesImgPath"
              class="flex flex-row items-center text-pcs-primary"
            >
              <svg-icon :size="44" type="mdi" :path="mdiPlus"></svg-icon>
              <span>
                add image
                <span class="text-error">*</span>
              </span>
            </p>
            <input
              ref="fileInput"
              type="file"
              id="fileInput"
              style="display: none"
              accept="image/*"
            />
          </div>

          <div class="ml-10">
            <form action="" class="">
              <div class="relative w-full flex flex-col">
                <div class="whitespace-nowrap flex flex-row">
                  <span style="width: 14rem">
                    species name
                    <span class="text-error">*</span>
                    ：
                  </span>

                  <input
                    placeholder="species name"
                    style="width: 20rem"
                    v-model="speciesInfoForm.latinName"
                    type="text"
                    class="input input-sm border border-pcs-border border-solid"
                  />
                </div>
              </div>

              <div class="relative w-full flex flex-col mt-2">
                <div class="whitespace-nowrap flex flex-row">
                  <span style="width: 14rem">
                    species classification
                    <span class="text-error">*</span>
                    ：
                  </span>

                  <select
                    style="width: 20rem"
                    v-model="speciesInfoForm.popularClassification"
                    class="select select-sm text-xs select-bordered border-pcs-border max-w-xs"
                  >
                    <option v-for="item in popularCla" :value="item.id">
                      {{ item.title }}
                    </option>
                  </select>
                </div>
              </div>

              <div class="relative w-full flex flex-col mt-2">
                <div class="whitespace-nowrap flex flex-row">
                  <span style="width: 14rem">
                    growth season
                    <span class="text-error">*</span>
                    ：
                  </span>

                  <select
                    style="width: 20rem"
                    v-model="speciesInfoForm.seasonClassification"
                    class="select select-sm text-xs select-bordered border-pcs-border"
                  >
                    <option v-for="item in seasonCla" :value="item.id">
                      {{ item.title }}
                    </option>
                  </select>
                </div>
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">species description：</span>
                <input
                  style="width: 20rem"
                  placeholder="Describe this species"
                  v-model="speciesInfoForm.descp"
                  type="text"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">growth area：</span>
                <input
                  type="text"
                  placeholder="What area does it grow in?"
                  style="width: 20rem"
                  v-model="speciesInfoForm.growthArea"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">optimal temperature：</span>
                <label
                  style="width: 20rem"
                  class="input input-sm border border-pcs-border border-solid flex justify-between"
                >
                  <input type="text" v-model="optimalTemperature" />
                  <span>℃</span>
                </label>
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">optimal soil：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="What type of soil is suitable for planting?"
                  v-model="speciesInfoForm.optimalSoil"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">growth time：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="When does it grow?"
                  v-model="speciesInfoForm.growthTime"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">harvest time：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="When to harvest?"
                  v-model="speciesInfoForm.harvestTime"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">optimal humidity：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="Optimal growth humidity"
                  v-model="speciesInfoForm.optimalHumidity"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">longitude：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="Which longitude does it grow at?"
                  v-model="speciesInfoForm.lng"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">latitude：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  placeholder="Which latitude does it grow at?"
                  v-model="speciesInfoForm.lat"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>

              <div class="mt-2 w-full whitespace-nowrap flex flex-row">
                <span style="width: 14rem">best company：</span>
                <input
                  style="width: 20rem"
                  type="text"
                  v-model="speciesInfoForm.bestCompany"
                  placeholder="The best companies to buy seeds"
                  class="input input-sm border border-pcs-border border-solid"
                />
              </div>
            </form>
          </div>
        </div>

        <div class="modal-action">
          <button
            :disabled="!uploadSpeciesImgPath"
            class="btn bg-pcs-primary text-white"
            @click="saveSpecies"
          >
            save
          </button>
        </div>
      </div>
    </dialog>

    <dialog ref="userInfoEdit" id="userInfoEdit" class="modal">
      <div class="modal-box w-1/4 max-w-5xl">
        <p class="flex justify-end">
          <svg-icon
            @click="closeModal"
            type="mdi"
            class="cursor-pointer"
            :path="mdiWindowClose"
          ></svg-icon>
        </p>
        <h3 class="text-lg font-bold text-pcs-primary">
          Edit User Information
        </h3>

        <div class="flex flex-col justify-center items-center mt-2">
          <div
            class="w-full flex flex-col items-center justify-center mb-2"
            @click="uploadAvatar"
          >
            <input
              ref="avatarInput"
              @change="onAvatarChange"
              type="file"
              accept="image/*"
              class="hidden"
            />
            <img
              class="h-32 rounded-full"
              :src="!userInfo?.avatar ? noneHeadImg : '/img' + userInfo?.avatar"
              alt=""
            />
            <p
              class="pb-1 border-b border-solid border-pcs-primary text-gray-400 text-xs pl-5 pr-5"
            >
              Click edit your profile picture
            </p>
          </div>

          <form class="w-full flex flex-col gap-y-2">
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">username:</span>
              <input
                v-model="userInfo.username"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">password:</span>
              <input
                v-model="userInfo.password"
                type="password"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">age:</span>
              <input
                v-model="userInfo.age"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">gender:</span>
              <input
                v-model="userInfo.gender"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">phone:</span>
              <input
                type="tel"
                v-model="userInfo.phone"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">email:</span>
              <input
                type="email"
                disabled
                v-model="userInfo.email"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
            <label for="" class="flex flex-row items-center">
              <span class="block w-32">location:</span>
              <input
                v-model="userInfo.location"
                class="input w-full input-sm border-pcs-border border-solid border"
              />
            </label>
          </form>
        </div>

        <div class="modal-action">
          <button class="btn bg-pcs-primary text-white" @click="saveUserInfo">
            save
          </button>
        </div>
      </div>
    </dialog>
  </main>
</template>

<style scoped></style>
