<script setup lang="ts">
//@ts-ignore
import loginBg from '@/assets/img/LoginBg.png'
//@ts-ignore
import LogoBg from '@/assets/img/logo_bg.png'
//@ts-ignore
import { ref, watch } from 'vue'
import { getAssetsResource } from '@/utils/resource'
import * as userApi from '@/api/user'
import { showAlert } from '@/utils/ComponentRegister'
import {
  encodePassword,
  isValidCombination,
  validateEmail,
} from '@/utils/CommonUtil'
import { mdiEyeOffOutline, mdiEyeOutline } from '@mdi/js'
import { useRouter } from 'vue-router'

const router = useRouter()
const bgPath = getAssetsResource('./../assets/img/LoginBgBig.png')

const userForm = ref({
  username: '',
  password: '',
  email: '',
  gender: null,
  verCode: '',
})

const confirmPassword = ref('')

const register = (event) => {
  event.preventDefault()
  const temp = JSON.parse(JSON.stringify(userForm.value))
  temp.password = encodePassword(temp.password)
  userApi.register(temp).then((res) => {
    showAlert('Registration successful,user now!', 'info')
    toHome()
  })
}

const showPassword = ref(false)
const eye = ref()
const eyeClick = () => {
  showPassword.value = !showPassword.value
  if (showPassword.value) {
    eye.value.type = 'text'
  } else {
    eye.value.type = 'password'
  }
}

const toLogin = () => {
  router.push('/login')
}
const toHome = () => {
  router.push('/home')
}

const verSent = ref(false)
const sendRegisterVer = (e) => {
  e.preventDefault()
  let combination = isValidCombination(userForm.value.password)
  if (!combination) {
    showAlert('The password does not meet the rules', 'warning')
    return
  }
  if (validateEmail(userForm.value.email)) {
    userApi.sendRegisterEmail(userForm.value.email).then((res) => {
      verSent.value = true
    })
  } else {
    showAlert('Please enter a valid email', 'warning')
  }
}
</script>

<template>
  <div
    class="w-screen h-screen bg-cover flex justify-center items-center"
    :style="`background-image: url(${bgPath})`"
  >
    <div class="w-1/2 h-1/2">
      <div class="flex flex-row justify-between items-center">
        <span class="text-pcs-primary font-bold text-2xl">
          <img :src="LogoBg" class="h-10" alt="LOGO" />
        </span>
        <p>
          <span class="text-gray-400">already have an account,</span>
          <span
            class="text-pcs-primary font-bold cursor-pointer"
            @click="toLogin"
          >
            login immediately
          </span>
          <span>&nbsp;|&nbsp;</span>
          <span
            @click="toHome"
            class="text-pcs-primary font-bold cursor-pointer"
          >
            return to homepage
          </span>
        </p>
      </div>

      <div class="relative">
        <img :src="loginBg" alt="" />
        <h1
          class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-white"
        >
          Register to PCS
        </h1>
      </div>

      <div
        class="pt-8 shadow w-full h-full rounded border border-solid border-pcs-border bg-white"
      >
        <form class="p-2 flex flex-col gap-y-2">
          <div class="flex flex-row justify-center">
            <p class="flex flex-row items-center w-80">
              <span class="w-32">username：</span>
              <label
                class="border input-sm border-solid border-pcs-border w-full input flex items-center"
              >
                <input v-model="userForm.username" />
              </label>
            </p>

            <p class="ml-4 w-80 flex flex-row items-center">
              <span>gender：</span>
              <span class="flex flex-row items-center">
                <span class="mr-1">man</span>
                <input
                  v-model="userForm.gender"
                  type="radio"
                  name="gender"
                  value="male"
                  class="radio radio-success radio-sm"
                />
              </span>

              <span class="ml-4 flex flex-row items-center">
                <span class="mr-1">women</span>
                <input
                  v-model="userForm.gender"
                  type="radio"
                  name="gender"
                  value="female"
                  class="radio radio-success radio-sm"
                />
              </span>
            </p>
          </div>

          <div class="flex flex-row justify-center">
            <p class="flex flex-row items-center w-80">
              <span class="w-32">password：</span>

              <label
                class="border input-sm border-solid border-pcs-border w-full input flex items-center"
              >
                <input type="password" ref="eye" v-model="userForm.password" />
              </label>
            </p>

            <p
              class="ml-4 w-80 flex flex-row items-center text-xs whitespace-normal break-words"
            >
              please use a combination of uppercase and lowercase letters and
              numbers (6-16 characters).
            </p>
          </div>

          <div class="flex flex-row justify-center">
            <p class="flex flex-row items-center w-80">
              <span class="w-32">confirm password：</span>
              <label
                class="border input-sm border-solid border-pcs-border w-full input flex items-center"
              >
                <input ref="eye" type="password" v-model="confirmPassword" />
              </label>
            </p>

            <p
              v-if="!confirmPassword"
              class="ml-4 w-80 flex flex-row items-center text-xs whitespace-normal break-words"
            >
              please use a combination of uppercase and lowercase letters and
              numbers (6-16 characters).
            </p>
            <p
              v-else
              class="ml-4 w-80 flex flex-row items-center text-xs whitespace-normal break-words"
            >
              <span
                v-if="confirmPassword == userForm.password"
                class="text-pcs-primary"
              >
                Input consistency
              </span>
              <span v-else class="text-error">
                Password input does not match.
              </span>
            </p>
          </div>

          <div class="flex flex-row justify-center">
            <p class="flex flex-row items-center w-80">
              <span class="w-32">email：</span>
              <label
                class="border input-sm border-solid border-pcs-border w-full input flex items-center"
              >
                <input v-model="userForm.email" />
              </label>
            </p>

            <p
              class="ml-4 w-80 flex flex-row items-center text-xs whitespace-normal break-words"
            >
              <span v-if="!userForm.email">
                please fill in your email address for future password recovery.
              </span>

              <button
                v-if="userForm.email && validateEmail(userForm.email)"
                class="btn btn-sm bg-pcs-primary text-white"
                @click="sendRegisterVer"
              >
                Send verification code
              </button>

              <span
                v-if="userForm.email && !validateEmail(userForm.email)"
                class="text-error"
              >
                Please enter a valid email.
              </span>
            </p>
          </div>

          <div v-if="verSent" class="flex flex-row justify-center">
            <p class="flex flex-row items-center w-80">
              <span class="w-32">verification code：</span>
              <label
                class="border input-sm border-solid border-pcs-border w-full input flex items-center"
              >
                <input v-model="userForm.verCode" />
              </label>
            </p>

            <p
              class="ml-4 w-80 flex flex-row items-center text-xs whitespace-normal break-words"
            >
              <span>
                Please enter the verification code received in your email.
              </span>
            </p>
          </div>

          <p class="flex justify-center mt-2">
            <button
              @click="register"
              :disabled="!verSent"
              class="btn w-48 bg-pcs-primary text-white cursor-pointer"
            >
              submit registration
            </button>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
