<template>
  <div class="collections-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title>Collections</v-app-bar-title>
    </v-app-bar>
    <v-list>
      <v-list-item
        v-for="collection in collections"
        :key="collection.collection_id"
      >
        <template v-slot:title>
          <v-chip color="primary" @click="goDetail(collection.id)">{{ collection.name }}</v-chip>
        </template>
        <template v-slot:append>
          <v-btn @click="deleteCollection(collection.collection_id)" icon="mdi-delete" size="small" color="red"></v-btn>
        </template>
        <v-divider></v-divider>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
import request from '@/util/request'
import { STATUS_OK } from '@/util/constants'
import { mapActions } from "vuex"
export default {
  name: "Collections",
  data() {
    return {
      collections: []
    };
  },
  mounted() {
    this.getCollections();
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    async getCollections() {
      const res = await request.get("/plantCollections/list");
      // console.log("new collections: ", res.data);
      if (STATUS_OK === res.data.code) {
        this.collections = res.data.data.plantCollections;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    goBack() {
      this.$router.push("/profile");
    },
    async deleteCollection(id) {
      const res = await request.delete(`/plantCollections/${id}`);
      if (STATUS_OK === res.data.code) {
        this.getCollections();
      } else {
        this.acSetError(res.data.msg);
      }
    },
    goDetail(plantId) {
      this.$router.push(`/plants/${plantId}`);
    }
  }
}
</script>

<style scoped>
  .forum-container {
    height: 100%;
  }
</style>