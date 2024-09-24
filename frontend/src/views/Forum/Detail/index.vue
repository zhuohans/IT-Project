<template>
  <div class="plant-detail-container">
    <v-app-bar color="primary">
      <template v-slot:prepend>
        <v-btn @click="goBack()" icon="mdi-arrow-left"></v-btn>
      </template>
      <v-app-bar-title v-if="post">{{ post.title }} <small>created by {{ post.creator }}</small></v-app-bar-title>
    </v-app-bar>
    <v-card v-if="post">
      <div v-if="post.images">
        <v-img
          v-for="image in post.images"
          :key="image"
          height="200px"
          :src="`${host}`+image"
          cover
        ></v-img>
      </div>
      <div>
        <v-divider></v-divider>
        <v-card-text class="description">
          {{ post.content }}
        </v-card-text>
        <v-card-text class="description">
          {{ formatDate(post.created_at) }}
        </v-card-text>
      </div>
    </v-card>
    <v-card v-if="token">
      <v-card-title>
        Comment
      </v-card-title>
      <form @submit.prevent="submit">
        <v-text-field
          v-model="content"
          label="Content"
        ></v-text-field>
        <v-btn
          class="me-4"
          color="primary"
          block
          type="submit"
          @click="handleSubmit"
        >
          Submit
        </v-btn>
      </form>
    </v-card>

    <v-list v-if="comments && comments.length > 0">
      <v-list-item
        v-for="comment in comments"
        :key="comment.id"
        :prepend-avatar="`${host}${comment.commenter_avatar}`"
        :subtitle="formatDate(comment.created_at)"
      >
        <template v-slot:title>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>{{ comment.content }}</div>
          </div>
        </template>
        <v-divider></v-divider>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
import { STATUS_OK, HOST } from '@/util/constants'
import { mapActions, mapState } from 'vuex'
import request from '@/util/request'
import { formattedDate } from '@/util/dateUtil'
export default {
  name: "PostDetail",
  data() {
    return {
      postId: null,
      post: null,
      host: HOST,
      content: '',
      comments: []
    };
  },
  computed: {
    ...mapState('user', ['token']),
  },
  mounted() {
    console.log("id: ", this.$route.params.id);
    this.postId = this.$route.params.id;
    this.getPostDetail(this.postId);
    this.getComments();
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    goBack() {
      this.$router.push("/forum");
    },
    async getPostDetail(id) {
      const res = await request.get(`/posts/${id}`);
      console.log(res.data.data.post);
      if (STATUS_OK === res.data.code) {
        this.post = res.data.data.post;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    formatDate(val) {
      return formattedDate(val)
    },
    async getComments() {
      const res = await request.get(`/posts/comments/${this.postId}`);
      console.log('comments', res.data.data);
      if (STATUS_OK === res.data.code) {
        this.comments = res.data.data.comments;
      } else {
        this.acSetError(res.data.msg);
      }
    },
    async handleSubmit() {
      const content = this.content;
      if (!content) {
        this.acSetError("Please enter comment content.");
        return;
      }
      const res = await request.post(`/posts/${this.postId}/comment`, { content });
      if (STATUS_OK === res.data.code) {
        this.acSetError("Success");
        this.content = "";
        this.getComments();
      } else {
        this.acSetError(res.data.msg);
      }
    }
  }
}
</script>

<style>

</style>