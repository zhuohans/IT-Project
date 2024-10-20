<script setup lang="ts">
//@ts-ignore
import logo from '@/assets/img/logo.png'
import * as userApi from '@/api/user/index'
import { reactive, ref } from 'vue'
import { mdiEyeOutline, mdiEyeOffOutline, mdiAccountQuestion } from '@mdi/js'
import { encodePassword } from '@/utils/CommonUtil'
import { useRouter } from 'vue-router'
import { showAlert } from '@/utils/ComponentRegister'
import { getAssetsResource } from '@/utils/resource'
//@ts-ignore
import loginBg from '@/assets/img/LoginBg.png'
//@ts-ignore
import loginBg1 from '@/assets/img/LoginBg1.png'

const bgPath = getAssetsResource('./../assets/img/LoginBgBig.png')
const router = useRouter()

const userForm = reactive({ username: '', password: '' })
const showPassword = ref(false)

const loginClick = (event) => {
  event.preventDefault()
  const temp = JSON.parse(JSON.stringify(userForm))
  temp.password = encodePassword(temp.password)
  if (showAccountTips.value) {
    userApi.login(temp).then((res: any) => {
      if (res) {
        localStorage.setItem('yzxToken', res.token)
        router.push('/')
      }
    })
  } else {
    userApi.register(temp).then((res) => {
      showAlert('Registration successful,user now!', 'info')
      showAccountTips.value = true
    })
  }
}

const eye = ref()
const eyeClick = () => {
  showPassword.value = !showPassword.value
  if (showPassword.value) {
    eye.value.type = 'text'
  } else {
    eye.value.type = 'password'
  }
}

const showAccountTips = ref(true)
const register = () => {
  router.push('/register')
}
const toForgotPassword = () => {
  router.push('/forgotPassword')
}
</script>

<template>
  <main
    class="w-full h-full flex justify-center items-center"
    :style="`background-image: url(${bgPath})`"
  >
    <section
      class="bg-white w-1/2 h-1/2 flex flex-row items-center card shadow gap-y-2"
    >
      <div class="h-full relative">
        <img class="h-full" :src="loginBg1" alt="" />
        <h1
          class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-white"
        >
          <img :src="logo" alt="" />
        </h1>
      </div>

      <div class="flex flex-col ml-8 w-3/5">
        <div class="flex flex-col">
          <div class="mb-8">
            <p class="text-pcs-primary font-bold">Welcome to log in.</p>
            <p class="text-pcs-primary font-bold">PCS</p>
          </div>

          <form class="mr-2 flex flex-col justify-center items-center gap-y-2">
            <label
              class="w-full input flex items-center border border-solid border-pcs-border"
            >
              <input
                v-model="userForm.username"
                type="text"
                placeholder="username"
              />
            </label>

            <label
              class="w-full input flex items-center border border-solid border-pcs-border"
            >
              <input
                v-model="userForm.password"
                ref="eye"
                placeholder="password"
                type="password"
                class="w-full"
              />
              <span class="cursor-pointer" @click="eyeClick">
                <svg-icon
                  v-show="showPassword"
                  type="mdi"
                  :path="mdiEyeOutline"
                />
                <svg-icon
                  v-show="!showPassword"
                  type="mdi"
                  :path="mdiEyeOffOutline"
                />
              </span>
            </label>

            <p
              class="flex flex-row justify-end w-full text-pcs-primary mt-2 mb-2"
            >
              <span
                class="flex flex-row gap-x-1 cursor-pointer"
                @click="toForgotPassword"
              >
                forgot password
                <svg-icon type="mdi" :path="mdiAccountQuestion"></svg-icon>
              </span>
            </p>

            <button
              class="btn w-48 bg-pcs-primary text-white cursor-pointer"
              @click="loginClick"
            >
              {{ showAccountTips ? 'Login' : 'Register' }}
            </button>
          </form>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped></style>
