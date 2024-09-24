<template>
  <div class="plant-detail-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title v-if="plant">{{ plant.name }}</v-app-bar-title>
    </v-app-bar>
    <v-card v-if="plant">
      <v-img
        height="200px"
        :src="`${host}`+plant.plant_image"
        cover
      ></v-img>
      <v-fab
        v-if="token"
        class="ms-4"
        icon="mdi-star"
        location="top start"
        size="small"
        absolute
        offset
        :color="`${status ? 'red': 'primary'}`"
        @click="addToCollection()"
      ></v-fab>
      <v-list>
        <v-list-item
          :title="`Light: ${plant.light}`"
        >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
            :title="`Water: ${plant.water}`"
          >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
            :title="`Soil: ${plant.soil}`"
          >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
            :title="`Temperature: ${plant.temperature}`"
          >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
            :title="`Humidity: ${plant.humidity}`"
          >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
            :title="`Fertilization: ${plant.fertilization}`"
          >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item
          :title="`Harvesting: ${plant.harvesting}`"
        >
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item style="text-align: justify;">
          {{ plant.description }}
        </v-list-item>
        <v-divider></v-divider>
      </v-list>
    </v-card>
    
  </div>
</template>

<script>
import { STATUS_OK, HOST } from '@/util/constants'
import { mapActions, mapState } from 'vuex'
import request from '@/util/request'
export default {
  name: "PlantDetail",
  data() {
    return {
      plantId: null,
      plant: null,
      host: HOST,
      status: false
    };
  },
  computed: {
    ...mapState('user', ['token']),
  },
  mounted() {
    console.log("id: ", this.$route.params.id);
    this.plantId = this.$route.params.id;
    this.getPlantDetail(this.plantId);
    if (this.token) {
      this.getPlantStatus(this.plantId);
    }
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    goBack() {
      this.$router.push("/");
    },
    async getPlantDetail(id) {
      const res = await request.get(`/plants/${id}`);
      console.log(res);
      if (STATUS_OK === res.data.code) {
        this.plant = res.data.data.plant;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    async getPlantStatus(id) {
      const res = await request.get(`/plantCollections/getPlantStatus/${id}`);
      if (STATUS_OK === res.data.code) {
        this.status = true;
      } else {
        this.status = false;
      }
    },
    async addToCollection() {
      const res = await request.post('/plantCollections/create', { plant_id: this.plantId });
      console.log(res.data);
      this.acSetError(res.data.msg);
      this.getPlantStatus(this.plantId);
    }
  }
}
</script>

<style>

</style>