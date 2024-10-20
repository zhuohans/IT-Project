<script setup lang="ts">
import { inject, ref } from 'vue'
import { UserPo } from '@/api/user/type'
import * as userApi from '@/api/user/index'
import { mdiPlus, mdiWindowClose } from '@mdi/js'
import { showAlert } from '@/utils/ComponentRegister'
import { encodePassword, isValidCombination } from '@/utils/CommonUtil'
import { useRouter } from 'vue-router'
import { uploadFile } from '@/api/user/index'

const router = useRouter()

const userInfo = inject('userInfo')
let isEdit = inject('isEdit')
const uploadAvatar = ref()
const changePassword = ref()
const avatarInput = ref()

const userMenu = ref([
  { id: '1', title: 'personal information', active: true },
  { id: '2', title: 'upload avatar', active: false },
  { id: '3', title: 'change password', active: false },
])

const tabClick = (item) => {
  for (let menu of userMenu.value) {
    menu.active = false
  }
  if (item.id == '2') {
    //上传图片
    uploadAvatar.value.showModal()
  }
  if (item.id == '3') {
    //上传图片
    changePassword.value.showModal()
  }
  item.active = true
}

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
      isEdit.value = false
    })
    .catch(() => {
      userInfo.value = temp
    })
}

const previewSrc = ref()
const avatarFile = ref()
const onAvatarChange = (e) => {
  let file = avatarInput.value.files[0]
  if (file) {
    avatarFile.value = file
    let reader = new FileReader()
    reader.onload = (e) => {
      previewSrc.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

const saveAvatar = () => {
  const formData = new FormData()
  formData.append('file', avatarFile.value, avatarFile.value.name)
  userApi.uploadFile(formData).then((res: any) => {
    userInfo.value.avatar = res
    userApi.updateUserInfo(userInfo.value)
    avatarFile.value = ''
    previewSrc.value = ''
  })
}

const changePasswordForm = ref({ newPassword: '', confirmPassword: '' })

const savePassword = () => {
  if (
    changePasswordForm.value.newPassword !=
    changePasswordForm.value.confirmPassword
  ) {
    showAlert('Password input does not match.', 'warning')
    return
  }
  if (!isValidCombination(changePasswordForm.value.newPassword)) {
    showAlert('The password does not meet the rules', 'warning')
    return
  }
  const encodedPassword = encodePassword(
    changePasswordForm.value.confirmPassword,
  )
  const temp = { username: userInfo?.username, password: encodedPassword }
  userApi.changePassword(temp).then((res) => {
    changePassword.value.close()
    showAlert('The password has been changed, please log in again.', 'info')
    localStorage.removeItem('yzxToken')
    router.push('/login')
  })
}

const closeAvatarModal = () => {
  uploadAvatar.value.close()
}
const closePasswordModal = () => {
  changePassword.value.close()
}
</script>

<template>
  <section class="mb-4 flex flex-row gap-x-2">
    <span
      v-for="item in userMenu"
      class="cursor-pointer p-2 text-pcs-primary rounded-xl border border-solid border-gray-300"
      :class="{ 'bg-pcs-second': item.active, 'text-white': item.active }"
      @click="tabClick(item)"
    >
      {{ item.title }}
    </span>
  </section>

  <section class="card shadow p-2 ml-2 mt-4 w-1/3">
    <form class="flex flex-col gap-y-2">
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">username:</span>
        <input
          v-if="isEdit"
          v-model="userInfo.username"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.username }}</span>
      </label>
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">age:</span>
        <input
          v-model="userInfo.age"
          v-if="isEdit"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.age }}</span>
      </label>
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">gender:</span>
        <input
          v-if="isEdit"
          v-model="userInfo.gender"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.gender }}</span>
      </label>
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">phone:</span>
        <input
          v-if="isEdit"
          type="tel"
          v-model="userInfo.phone"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.phone }}</span>
      </label>
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">email:</span>
        <input
          v-if="isEdit"
          type="email"
          disabled
          v-model="userInfo.email"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.email }}</span>
      </label>
      <label for="" class="flex flex-row items-center">
        <span class="block w-24">location:</span>
        <input
          v-if="isEdit"
          v-model="userInfo.location"
          class="input input-sm border-pcs-border border-solid border"
        />
        <span v-else>{{ userInfo.location }}</span>
      </label>
      <button
        @click="saveUserInfo"
        v-if="isEdit"
        class="btn w-24 bg-pcs-primary text-white"
      >
        save
      </button>
    </form>
  </section>

  <dialog ref="uploadAvatar" id="uploadAvatar" class="modal">
    <div class="modal-box">
      <p class="flex justify-end">
        <svg-icon
          @click="closeAvatarModal"
          type="mdi"
          class="cursor-pointer"
          :path="mdiWindowClose"
        ></svg-icon>
      </p>
      <h3 class="text-lg font-bold">upload avatar</h3>
      <p class="py-4">
        <span>Please choose your avatar.</span>
      </p>
      <label class="mt-2 flex flex-row gap-x-2">
        <input
          ref="avatarInput"
          @change="onAvatarChange"
          type="file"
          accept="image/*"
          class="hidden"
        />
        <p v-if="previewSrc" class="w-24 h-24 rounded">
          <img :src="previewSrc" alt="" />
        </p>
        <p
          class="cursor-pointer rounded p-4 bg-pcs-second w-24 h-24 flex items-center justify-center text-white"
        >
          <svg-icon :size="32" type="mdi" :path="mdiPlus"></svg-icon>
        </p>
      </label>

      <div class="modal-action">
        <form method="dialog">
          <!-- if there is a button in form, it will close the modal -->
          <button class="btn bg-pcs-primary text-white" @click="saveAvatar">
            save
          </button>
        </form>
      </div>
    </div>
  </dialog>

  <dialog ref="changePassword" id="changePassword" class="modal">
    <div class="modal-box">
      <p class="flex justify-end">
        <svg-icon
          @click="closePasswordModal"
          type="mdi"
          class="cursor-pointer"
          :path="mdiWindowClose"
        ></svg-icon>
      </p>
      <h3 class="text-lg font-bold">change password</h3>
      <form class="mt-2 flex flex-col gap-y-2 justify-center">
        <label for="" class="flex flex-row w-full">
          <p class="w-36 whitespace-nowrap">new password:</p>
          <input
            type="password"
            v-model="changePasswordForm.newPassword"
            class="input input-sm input-bordered border-solid border"
          />
        </label>

        <label for="" class="flex flex-row">
          <p class="w-36 whitespace-nowrap">confirm password:</p>
          <input
            type="password"
            v-model="changePasswordForm.confirmPassword"
            class="input input-sm input-bordered border-solid border"
          />
        </label>
      </form>

      <div class="modal-action">
        <button class="btn bg-pcs-primary text-white" @click="savePassword">
          save
        </button>
      </div>
    </div>
  </dialog>
</template>

<style scoped></style>
