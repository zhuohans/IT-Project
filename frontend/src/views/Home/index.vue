<template>
  <div class="home-container">
    <v-sheet elevation="6">
      <v-tabs
        bg-color="primary"
        next-icon="mdi-arrow-right-bold-box-outline"
        prev-icon="mdi-arrow-left-bold-box-outline"
        show-arrows
      >
        <v-tab
          v-for="category in plantCategories"
          :key="category.id"
          :text="category.name"
          @click="filterByCategory(category.id)"
        ></v-tab>
      </v-tabs>
    </v-sheet>
    <v-card v-for="plant in plants" :key="plant.id" class="card" @click="goDetail(`/plants/${plant.id}`)">
      <v-img
        height="200px"
        :src="`${host}`+plant.plant_image"
        cover
      ></v-img>

      <v-card-title>
        {{ plant.name }}
      </v-card-title>

      <div>
        <v-divider></v-divider>
        <v-card-text class="description">
          {{ plant.description }}
        </v-card-text>
      </div>
    </v-card>
  </div>
</template>

<script>
import { mapActions } from "vuex"
import request from '@/util/request'
import { STATUS_OK, HOST } from '@/util/constants'
export default {
  name: "Home",
  data() {
    return {
      plantCategories: [],
      plants: [],
      show: false,
      host: HOST
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    ...mapActions('notification', ['acSetError', 'acClearError']),
    init() {
      // this.acSetError("Internal error...");
      this.getPlants();
      this.getPlantCategories();
    },
    async getPlantCategories() {
      const res = await request.get('/categories/list');
      if (STATUS_OK === res.data.code) {
        // console.log(res.data.data);
        this.plantCategories = [{ name: "All", id: null }, ...res.data.data.categories];
      } else {
        this.acSetError(res.data.msg);
      }
    },
    async getPlants() {
      const res = await request.get('/plants/list');
      if (STATUS_OK === res.data.code) {
        // console.log(res.data.data);
        this.plants = res.data.data.plants;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    async filterByCategory(categoryId) {
      if (!categoryId) {
        this.getPlants();
      } else {
        const res = await request.get(`/plants/listByCategory/${categoryId}`);
        if (STATUS_OK === res.data.code) {
          console.log(res.data.data);
          this.plants = res.data.data.plants;
        } else {
          this.acSetError(res.data.msg);
        }
      }
    },
    goDetail(path) {
      this.$router.push(path);
    }
  }
}
</script>

<style scoped>
  .home-container {
    /* height: 100%; */
  }
  .home-container .card {
    padding: 1rem;
  }
  .home-container .card:not(:first-child) {
    margin-top: 1rem !important;
  }
  .card .description {
    text-align: justify !important;
  }
</style>