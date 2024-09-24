<template>
  <div class="edit-profile-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Upload avatar</v-app-bar-title>
    </v-app-bar>
    <form @submit.prevent="submit">
      <v-file-input
        v-model="avatar.value.value"
        accept="image/png, image/jpeg, image/bmp"
        label="Avatar"
        placeholder="Pick an avatar"
        prepend-icon="mdi-camera"
        :error-messages="avatar.errorMessage.value"
      ></v-file-input>
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
  import { useField, useForm } from 'vee-validate'
  import { useRouter } from 'vue-router'
  import { useStore } from 'vuex'
  import request from '@/util/request'
  import { STATUS_OK, TOKEN, USER_NAME, USER_ID } from '@/util/constants'
  import Notification from '@/components/notification'

  const { handleSubmit } = useForm({
    validationSchema: {
      avatar (value) {
        if (value) return true;
        return 'Please enter pick up one image as your avatar';
      },
    },
  })
  const avatar = useField('avatar')
  const router = useRouter();
  const store = useStore();

  const submit = handleSubmit(async (values) => {
    console.log(values);
    const formData = new FormData();  
    formData.append('avatar', values.avatar);
    const res = await request.post('/user/avatar/upload', formData, {
      headers: {  
          'Content-Type': 'multipart/form-data'  
      }  
    });
    if (STATUS_OK === res.data.code) {
      store.dispatch('notification/acSetError', res.data.msg);
      router.push('/profile');
    } else {
      store.dispatch('notification/acSetError', res.data.msg);
    }
  })

  const goBack = (path) => {
    router.push("/profile");
  }
</script>
<style scoped>
</style>