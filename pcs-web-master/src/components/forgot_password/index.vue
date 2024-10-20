<script setup lang="ts">
import { ref } from 'vue'
import * as userApi from '@/api/user/index'
import { showAlert } from '@/utils/ComponentRegister'
import {
  encodePassword,
  isValidCombination,
  validateEmail,
} from '@/utils/CommonUtil'
import { getAssetsResource } from '@/utils/resource'
import { useRouter } from 'vue-router'
import loginBg from '@/assets/img/LoginBg.png'

const bgPath = getAssetsResource('./../assets/img/LoginBgBig.png')
const router = useRouter()

const email = ref('')
const emailSent = ref(true)
const code = ref('')
const newPassword = ref('')

const sendEmail = (event) => {
  event.preventDefault()
  if (!email.value) {
    showAlert('Please enter a valid email address', 'warning')
    return
  } else {
    if (!validateEmail(email.value)) {
      showAlert('Please enter a valid email address', 'warning')
      return
    }
  }
  if (emailSent.value) {
    userApi.sendEmail(email.value).then(() => {
      emailSent.value = false
      showAlert('send success', 'info')
    })
  } else {
    if (!isValidCombination(newPassword.value)) {
      showAlert('The password does not meet the rules', 'warning')
      return
    }
    const temp = {
      email: email.value,
      code: code.value,
      newPassword: encodePassword(newPassword.value),
    }
    userApi.resetPassword(temp).then((res) => {
      router.push('/home')
      showAlert('reset password success', 'info')
    })
  }
}
</script>

<template>
  <main
    class="w-full h-full flex justify-center items-center"
    :style="`background-image: url(${bgPath})`"
  >
    <section
      class="w-1/3 bg-white border border-solid border-pcs-border flex flex-col items-center card shadow gap-y-2"
    >
      <div class="relative">
        <img :src="loginBg" alt="" />
        <h1
          class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-white"
        >
          Retrieve password
        </h1>
      </div>
      <form class="w-3/4 mb-8">
        <label class="w-full flex items-center">
          <span class="whitespace-nowrap w-56">Fill in the email：</span>
          <input
            v-model="email"
            type="text"
            class="w-full input border-pcs-border"
          />
        </label>

        <label class="w-full flex items-center mt-2">
          <span class="whitespace-nowrap w-56">code：</span>
          <input
            :disabled="emailSent"
            v-model="code"
            type="text"
            class="w-full input border-pcs-border"
          />
        </label>

        <label v-show="!emailSent" class="w-full flex items-center mt-2">
          <span class="whitespace-nowrap w-56">new password：</span>
          <input
            v-model="newPassword"
            type="password"
            class="w-full input border-pcs-border"
          />
        </label>

        <p class="flex justify-center">
          <button
            class="mt-4 btn w-48 bg-pcs-primary text-white cursor-pointer"
            @click="sendEmail"
          >
            {{ emailSent ? 'Send verification code' : 'Reset password' }}
          </button>
        </p>
      </form>
    </section>
  </main>
</template>

<style scoped></style>
