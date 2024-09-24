<template>
  <div class="reset-password-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Create Post</v-app-bar-title>
    </v-app-bar>
    <form @submit.prevent="submit">
      <v-text-field
        v-model="title.value.value"
        :error-messages="title.errorMessage.value"
        label="Title"
      ></v-text-field>

      <v-text-field
        v-model="content.value.value"
        :error-messages="content.errorMessage.value"
        label="Content"
        multiline
      ></v-text-field>
  
      <v-file-input
        v-model="images.value.value"
        accept="image/png, image/jpeg, image/bmp"
        label="Images"
        placeholder="Pick an avatar"
        prepend-icon="mdi-camera"
        multiple
        @change="onFileChange" 
        :error-messages="images.errorMessage.value"
      ></v-file-input>

      <v-card height="200" v-for="(file, index) in files" :key="index" class="pa-3" width="200">  
        <v-img :src="getUrl(file)" class="ma-0"></v-img>  
      </v-card> 
  
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
      title (value) {
        if (value) return true;
        return 'Please enter post title';
      },
      content (value) {
        if (value) return true;
        return 'Please enter title content';
      },
      images (value) {
        if (value) return true;
        return 'Please choose post images.'
      }
    },
  })
  const title = useField('title')
  const content = useField('content')
  const images = useField('images')
  const router = useRouter();
  const store = useStore();
  const files = ref([]);

  const onFileChange = (e) => {
    files.value = [];
    if (e.target.files && e.target.files.length) {  
      for (let i = 0; i < e.target.files.length; i++) {  
        files.value.push(e.target.files[i]);  
      }  
    }  
  }

  const getUrl = (file) => {
    return URL.createObjectURL(file);
  }

  const submit = handleSubmit(async (values) => {
    console.log(values.images);
    const formData = new FormData();  
    if (values.images && values.images.length > 0) {
      for (const image of values.images) {
        formData.append('images', image);
      }
    }
    formData.append('title', values.title);
    formData.append('content', values.content);
    const res = await request.post('/posts/create', formData, {
      headers: {  
        'Content-Type': 'multipart/form-data'  
      }  
    });
    if (STATUS_OK === res.data.code) {
      store.dispatch('notification/acSetError', res.data.msg);
      router.push('/forum');
    } else {
      store.dispatch('notification/acSetError', res.data.msg);
    }
  })

  const goBack = () => {
    router.push("/forum");
  }
</script>
<style scoped>
</style>