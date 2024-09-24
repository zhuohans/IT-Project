<template>
  <div class="edit-profile-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Edit profile</v-app-bar-title>
    </v-app-bar>
    <form @submit.prevent="submit">
      <v-text-field
        v-model="age.value.value"
        :error-messages="age.errorMessage.value"
        label="Age"
        type="number"
        min="18"
      ></v-text-field>
  
      <v-select
        v-model="gender.value.value"
        :items="genderList"
        item-title="label"
        item-value="value"
        label="Gender"
        :error-messages="gender.errorMessage.value"
      ></v-select>
  
      <v-text-field
        v-model="phoneNumber.value.value"
        :error-messages="phoneNumber.errorMessage.value"
        label="Phone Number"
      ></v-text-field>
  
      <v-text-field
        v-model="address.value.value"
        :error-messages="address.errorMessage.value"
        label="Address"
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
  import { ref, onMounted } from 'vue'
  import { useField, useForm } from 'vee-validate'
  import { useRouter } from 'vue-router'
  import { useStore } from 'vuex'
  import request from '@/util/request'
  import { STATUS_OK, TOKEN, USER_NAME, USER_ID } from '@/util/constants'
  import Notification from '@/components/notification'

  const { handleSubmit } = useForm({
    validationSchema: {
      age (value) {
        if (value && value >= 18) return true;
        return 'Please enter your age and must be greater than 18.';
      },
      gender (value) {
        if (value) return true
        return 'Please choose your gender'
      },
      phoneNumber (value) {
        if (value) return true;
        return 'Please enter your phone number';
      },
      address (value) {
        if (value) return true;
        return 'Please enter your address'
      }
    },
  })
  const age = useField('age')
  const gender = useField('gender')
  const phoneNumber = useField('phoneNumber')
  const address = useField('address')
  const router = useRouter();
  const store = useStore();

  const genderList = [
    {
      label: "Female",
      value: 0
    },
    {
      label: "Male",
      value: 1
    }
  ];

  const submit = handleSubmit(async (values) => {
    const res = await request.put('/user/edit', values);
    console.log(res.data);
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

  const getProfile = async () => {
    const res = await request.get('/user/profile');
    console.log(res.data);
    if (STATUS_OK === res.data.code) {
      const user = res.data.data.user;
      age.value.value = user.age;
      gender.value.value = user.gender;
      phoneNumber.value.value = user.phone_number;
      address.value.value = user.address;
    } else {
      store.dispatch('notification/acSetError', res.data.msg);
    }
  }
  onMounted(() => {
    getProfile();
  });
</script>
<style scoped>
</style>