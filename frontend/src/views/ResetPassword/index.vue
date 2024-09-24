<template>
  <div class="reset-password-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Reset password</v-app-bar-title>
    </v-app-bar>
    <form @submit.prevent="submit">
      <v-text-field
        v-model="oldPassword.value.value"
        :error-messages="oldPassword.errorMessage.value"
        label="Old Password"
        type="password"
      ></v-text-field>

      <v-text-field
        v-model="newPassword.value.value"
        :error-messages="newPassword.errorMessage.value"
        label="new Password"
        type="password"
      ></v-text-field>
  
      <v-text-field
        v-model="confirmPassword.value.value"
        :error-messages="confirmPassword.errorMessage.value"
        label="Confirm Password"
        type="password"
      ></v-text-field>
  
      <v-btn
        class="me-4"
        color="primary"
        block
        type="submit"
      >
        Submit
      </v-btn>
      <Notification />
    </form>
  </div>
</template>
<script setup>
  import { ref } from 'vue'
  import { useField, useForm } from 'vee-validate'
  import { useRouter } from 'vue-router'
  import { useStore } from 'vuex'
  import request from '@/util/request'
  import { STATUS_OK, TOKEN, USER_NAME, USER_ID } from '@/util/constants'
  import Notification from '@/components/notification'

  const { handleSubmit } = useForm({
    validationSchema: {
      oldPassword (value) {
        if (value) return true;
        return 'Password enter old password';
      },
      newPassword (value) {
        if (/^[0-9-]{6,}$/.test(value)) return true;
        return 'Password need to be at least 6 digit.';
      },
      confirmPassword (value) {
        // console.log("password.value: ", password.value.value);
        if (value == newPassword.value.value) return true;
        return 'Please enter correct confirm password.'
      }
    },
  })
  const oldPassword = useField('oldPassword')
  const newPassword = useField('newPassword')
  const confirmPassword = useField('confirmPassword')
  const router = useRouter();
  const store = useStore();

  const submit = handleSubmit(async (values) => {
    delete values.confirmPassword;
    console.log(values);
    const res = await request.put('/user/resetPassword', { password: values.oldPassword, newPassword: values.newPassword });
    console.log(res.data);
    if (STATUS_OK === res.data.code) {
      store.dispatch('notification/acSetError', res.data.msg);
    } else {
      store.dispatch('notification/acSetError', res.data.msg);
    }
  })

  const goBack = () => {
    router.push("/profile");
  }
</script>
<style scoped>
  .tab-container {
    margin-bottom: 1rem;
  }
</style>