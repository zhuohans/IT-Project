<script setup lang="ts">
//@ts-ignore
import noneImg from '@/assets/img/NoneImg.png'
import {
  mdiChevronRight,
  mdiCommentProcessingOutline,
  mdiStar,
  mdiStarOutline,
  mdiThumbUp,
  mdiThumbUpOutline,
  mdiDeleteCircleOutline,
  mdiEyeOutline,
  mdiClose,
} from '@mdi/js'
//@ts-ignore
import noneHeadImg from '@/assets/img/NoneHeadImg.png'
//@ts-ignore
import speciesImg from '@/assets/img/species.png'
import * as homeApi from '@/api/home'
import { inject, ref } from 'vue'
import { onBeforeRouteLeave } from 'vue-router'
import router from '@/router'

const species = inject('speciesInfo')

const userInfo = inject('userInfo')

const likeClick = () => {
  const like = species.value?.likeStatus == 0 ? 1 : 0
  homeApi.likeSpecies(species.value?.id, like).then((res) => {
    species.value.likeStatus = like
    if (like == 1) {
      species.value.likeCnt++
    } else {
      species.value.likeCnt--
    }
  })
}
const collectClick = () => {
  const collect = species.value?.collectStatus == 0 ? 1 : 0
  homeApi.collectSpecies(species.value?.id, collect).then((res) => {
    species.value.collectStatus = collect
    if (collect == 1) {
      species.value.collectCnt++
    } else {
      species.value.collectCnt--
    }
  })
}

const comment = ref('')
const postComment = () => {
  homeApi.commentSpecies(species.value.id, comment.value).then((res) => {
    comment.value = ''
    species.value.commentList = res.commentList
  })
}
const imgLoadError = (e) => {
  const img = e.srcElement
  img.onerror = null
  img.src = noneImg
}

const delComment = (item) => {
  homeApi.delComment(item.id, item.speciesId).then((res) => {
    species.value.commentList = species.value?.commentList.filter(
      (it) => it.id != item.id,
    )
  })
}

const drawer = ref()
const isOpen = ref(false)
const closeDrawer = () => {
  isOpen.value = false
}
const isSpecies = inject('isSpecies')
onBeforeRouteLeave(() => {
  isSpecies.value = false
})

const popuCla = ref([
  { id: 0, active: false, type: 0, title: 'Flower' },
  { id: 1, active: false, type: 1, title: 'Vegetable' },
  { id: 2, active: false, type: 2, title: 'Tree' },
  { id: 3, active: false, type: 3, title: 'Fruit' },
])
const seasonCla = ref([
  { id: 0, active: false, type: 0, title: 'Spring' },
  { id: 1, active: false, type: 1, title: 'Summer' },
  { id: 2, active: false, type: 2, title: 'Autumn' },
  { id: 3, active: false, type: 3, title: 'Winter' },
])

const delDialog = ref(false)
const showDelDialog = () => {
  delDialog.value.showModal()
}
const isEdite = ref(false)
const delSpecies = () => {
  homeApi.deleteSpecies(species.value.id).then((res) => {
    isSpecies.value = false
    router.go(0)
  })
}
const closeDelDialog = () => {
  delDialog.value.close()
}

const updateSpecies = () => {
  if (
    species.value.optimalTemperature &&
    !species.value.optimalTemperature.includes('℃')
  ) {
    species.value.optimalTemperature = species.value.optimalTemperature + '℃'
  }
  homeApi.updateSpecies(species.value).then((res) => {
    isEdite.value = false
  })
}
defineExpose({ isEdite })
</script>

<template>
  <div class="mb-2 mt-2 flex flex-row gap-x-10">
    <h1 class="font-bold text-xl">{{ species?.latinName }}</h1>
    <span>work number: {{ species?.id }}</span>
    <span>upload time: {{ species?.createTime }}</span>
    <button
      class="btn btn-xs bg-pcs-primary text-white"
      @click="isEdite = !isEdite"
      v-if="species?.createBy == userInfo?.userId"
    >
      edit
    </button>
    <button
      class="btn btn-xs bg-error text-white"
      @click="showDelDialog"
      v-if="species?.createBy == userInfo?.userId"
    >
      delete
    </button>
  </div>

  <div class="h-full flex flex-row gap-x-4">
    <div
      class="relative w-2/3 rounded-xl bg-pcs-species p-4 flex justify-center items-center"
    >
      <img
        class="h-96"
        :src="species?.imgPath ? '/img' + species?.imgPath : noneImg"
        :onerror="imgLoadError"
        alt=""
      />

      <div
        class="absolute top-1/2 -translate-y-1/2 right-2 flex flex-col gap-x-2 mt-4"
      >
        <p
          @click="likeClick"
          class="cursor-pointer flex flex-col items-center justify-center w-10 h-10 p-1 bg-pcs-primary text-white rounded-full"
        >
          <svg-icon
            type="mdi"
            size="24"
            v-if="species?.likeStatus == 0"
            :path="mdiThumbUpOutline"
          ></svg-icon>
          <svg-icon type="mdi" size="24" v-else :path="mdiThumbUp"></svg-icon>
          <span class="text-xs">
            {{ species?.likeCnt }}
          </span>
        </p>

        <p
          @click="collectClick"
          class="cursor-pointer mt-2 flex flex-col items-center justify-center w-10 h-10 p-1 bg-pcs-primary text-white rounded-full"
        >
          <svg-icon
            type="mdi"
            v-if="species?.collectStatus == 0"
            size="24"
            :path="mdiStarOutline"
          ></svg-icon>
          <svg-icon type="mdi" v-else size="24" :path="mdiStar"></svg-icon>
          <span class="text-xs">{{ species?.collectCnt }}</span>
        </p>

        <p
          class="mt-2 cursor-pointer flex flex-col items-center justify-center w-10 h-10 p-1 bg-pcs-primary text-white rounded-full"
        >
          <svg-icon type="mdi" size="24" :path="mdiEyeOutline"></svg-icon>
          <span class="text-xs">
            {{ species?.viewCnt }}
          </span>
        </p>
      </div>
    </div>

    <div class="w-1/3 flex flex-col">
      <div
        class="card shadow flex flex-col border border-solid border-pcs-border p-2 rounded gap-y-2 h-96 overflow-auto scroll-hidden"
      >
        <div class="flex flex-row gap-x-2">
          <img
            class="rounded-full w-14 h-14"
            :src="
              species?.userInfo?.avatar
                ? '/img/' + species?.userInfo?.avatar
                : noneHeadImg
            "
            alt=""
          />

          <div>
            <p class="font-bold">{{ species?.userInfo?.username }}</p>
            <p>{{ species?.userInfo?.location }}</p>
          </div>
        </div>

        <div class="flex flex-row">
          <p class="w-44">species description：</p>
          <input
            v-model="species.descp"
            v-if="isEdite"
            type="text"
            class="input input-xs border border-solid border-pcs-primary w-44"
          />
          <span v-else>
            {{ species?.descp }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">species classification：</p>

          <select
            v-if="isEdite && species"
            v-model="species.popularClassification"
            class="select select-xs text-xs select-bordered border-pcs-primary w-44"
          >
            <option v-for="item in popuCla" :value="item.id">
              {{ item.title }}
            </option>
          </select>

          <span v-else>
            {{
              popuCla.filter(
                (it) => it.type == species?.popularClassification,
              )[0]?.title
            }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">growth season：</p>
          <select
            v-if="isEdite"
            v-model="species.seasonClassification"
            class="select select-xs text-xs select-bordered border-pcs-primary w-44"
          >
            <option v-for="item in seasonCla" :value="item.id">
              {{ item.title }}
            </option>
          </select>
          <span v-else>
            {{
              seasonCla.filter(
                (it) => it.type == species?.seasonClassification,
              )[0]?.title
            }}
          </span>
        </div>
        <div class="flex flex-row">
          <p class="w-44">growth area：</p>

          <input
            v-model="species.growthArea"
            v-if="isEdite"
            type="text"
            class="input input-xs border border-solid border-pcs-primary w-44"
          />
          <span v-else>
            {{ species?.growthArea }}
          </span>
        </div>
        <div class="flex flex-row">
          <p class="w-44">optimal temperature：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.optimalTemperature" />
            <span>℃</span>
          </label>
          <span v-else>
            {{ species?.optimalTemperature }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">optimal soil：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.optimalSoil" />
          </label>
          <span v-else>
            {{ species?.optimalSoil }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">growth time：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.growthTime" />
          </label>
          <span v-else>
            {{ species?.growthTime }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">harvest time：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.harvestTime" />
          </label>
          <span v-else>
            {{ species?.harvestTime }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">optimal humidity：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.optimalHumidity" />
          </label>
          <span v-else>
            {{ species?.optimalHumidity }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">longitude：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.lng" />
          </label>
          <span v-else>
            {{ species?.lng }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">latitude：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.lat" />
          </label>
          <span v-else>
            {{ species?.lat }}
          </span>
        </div>

        <div class="flex flex-row">
          <p class="w-44">best company：</p>
          <label
            v-if="isEdite"
            class="input input-xs border border-solid border-pcs-primary w-44"
          >
            <input type="text" v-model="species.bestCompany" />
          </label>
          <span v-else>
            {{ species?.bestCompany }}
          </span>
        </div>

        <div v-if="isEdite" class="flex justify-end">
          <button
            class="btn btn-xs bg-pcs-primary text-white"
            @click="updateSpecies"
          >
            save
          </button>
        </div>
      </div>

      <!--      展示3条评论        -->
      <div
        class="card shadow mt-2 h-full relative flex flex-col border border-solid border-pcs-border p-2 rounded"
      >
        <div class="flex flex-col justify-center mt-2">
          <div class="w-full flex flex-row">
            <img
              class="w-10 h-10 rounded-full"
              :src="userInfo?.avatar ? '/img/' + userInfo?.avatar : noneHeadImg"
              alt=""
            />
            <textarea
              v-model="comment"
              class="ml-2 w-full textarea textarea-success"
              placeholder="Say a few words"
            ></textarea>
          </div>
          <p class="flex justify-end mt-2">
            <button
              :disabled="!comment"
              @click="postComment"
              class="btn btn-xs bg-pcs-primary text-white"
            >
              post a comment
            </button>
          </p>
        </div>

        <div class="flex flex-row mt-2">
          <div
            v-if="species?.commentList?.length == 0"
            class="flex flex-col items-center w-full"
          >
            <img :src="noneImg" class="w-32" alt="" />
            <p class="mt-2">Be the first to comment!</p>
          </div>

          <div
            v-else
            class="flex flex-col overflow-auto h-[300px] scroll-hidden"
          >
            <div v-for="(item, index) in species?.commentList">
              <div class="flex flex-row pt-1">
                <img
                  class="rounded-full w-10 h-10"
                  :src="
                    item.userInfo?.avatar
                      ? '/img/' + item.userInfo?.avatar
                      : noneHeadImg
                  "
                  alt=""
                />
                <div class="ml-2 flex flex-col">
                  <p>
                    <span>{{ item.userInfo?.username }}</span>
                    <span class="text-gray-400">
                      &nbsp;{{ item.createTime }}
                    </span>
                  </p>
                  <span>
                    {{
                      item.comment.length > 100
                        ? item.comment.substring(0, 100) + '...'
                        : item.comment
                    }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <dialog ref="delDialog" id="delDialog" class="modal">
    <div class="modal-box">
      <h3 class="text-lg font-bold flex flex-row justify-between">
        Delete species!
        <svg-icon
          type="mdi"
          :path="mdiClose"
          class="cursor-pointer"
          @click="closeDelDialog"
        ></svg-icon>
      </h3>
      <p class="py-4">Are you sure you want to delete this species?</p>
      <div class="modal-action">
        <form method="dialog">
          <!-- if there is a button in form, it will close the modal -->
          <button class="btn bg-error text-white" @click="delSpecies">
            confirm
          </button>
        </form>
      </div>
    </div>
  </dialog>
</template>

<style scoped></style>
