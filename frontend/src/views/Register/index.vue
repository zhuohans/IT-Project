<template>
  <form class="login-container" @submit.prevent="submit">
    <v-tabs
      v-model="tab"
      bg-color="primary"
      align-tabs="center"
      class="tab-container"
    >
      <v-tab value="login" @click="go('/login')">Login</v-tab>
      <v-tab value="register" @click="go('/register')">Register</v-tab>
    </v-tabs>
    <v-text-field
      v-model="username.value.value"
      :counter="7"
      :error-messages="username.errorMessage.value"
      label="User name"
    ></v-text-field>

    <v-text-field
      v-model="email.value.value"
      :counter="7"
      :error-messages="email.errorMessage.value"
      label="Email"
    ></v-text-field>

    <v-text-field
      v-model="password.value.value"
      :error-messages="password.errorMessage.value"
      label="Password"
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
      Register
    </v-btn>
    <Notification />
  </form>
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
      username (value) {
        if (value) return true;
        return 'Please enter user name.';
      },
      email (value) {
        if (/^[a-zA-Z0-9.-]+@[a-z.-]+\.[a-z]+$/i.test(value)) return true
        return 'Must be a valid e-mail.'
      },
      password (value) {
        if (/^[0-9-]{6,}$/.test(value)) return true;
        return 'Password need to be at least 6 digit.';
      },
      confirmPassword (value) {
        // console.log("password.value: ", password.value.value);
        if (value == password.value.value) return true;
        return 'Please enter correct confirm password.'
      }
    },
  })
  const tab = ref("register");
  const username = useField('username')
  const email = useField('email')
  const password = useField('password')
  const confirmPassword = useField('confirmPassword')
  const router = useRouter();
  const store = useStore();

  const submit = handleSubmit(async (values) => {
    delete values.confirmPassword;
    console.log(values);
    const res = await request.post('/user/register', values);
    console.log(res.data);
    if (STATUS_OK === res.data.code) {
      store.dispatch('notification/acSetError', 'Register successfully!');
      router.push('/login');
    } else {
      store.dispatch('notification/acSetError', res.data.msg);
    }
  })

  const go = (path) => {
    router.push(path);
  }
</script>
<style scoped>
  .login-container {
    padding: 1rem;
  }
  .tab-container {
    margin-bottom: 1rem;
  }
</style>