<template>
  <v-card
    class="mx-auto forum-container"
  >
    <v-toolbar color="primary">
      <v-btn icon="mdi-menu" variant="text"></v-btn>

      <v-toolbar-title>Forum</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn v-if="token" @click="addForum()" icon="mdi-plus" variant="text"></v-btn>
    </v-toolbar>

    <v-list>
      <v-list-item
        v-for="post in posts"
        :key="post.id"
        :prepend-avatar="`${host}${post.creator_avatar}`"
        :subtitle="post.content"
        @click="goDetail(post.id)"
      >
        <template v-slot:title>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>{{ post.title }}</div> <small>{{ formatDate(post.created_at) }}</small>
          </div>
        </template>
        <v-divider></v-divider>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
import request from '@/util/request'
import { STATUS_OK, HOST } from '@/util/constants'
import { mapActions, mapState } from "vuex"
import { formattedDate } from '@/util/dateUtil'
export default {
  name: "Forum",
  data() {
    return {
      host: HOST,
      items: [
        {
          prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          title: 'Brunch this weekend?',
          subtitle: `<span class="text-primary">Ali Connors</span> &mdash; I'll be in your neighborhood doing errands this weekend. Do you want to hang out?`,
        },
        { type: 'divider', inset: true },
        {
          prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
          title: 'Summer BBQ',
          subtitle: `<span class="text-primary">to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend.`,
        },
        { type: 'divider', inset: true },
        {
          prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
          title: 'Oui oui',
          subtitle: '<span class="text-primary">Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?',
        },
        { type: 'divider', inset: true },
        {
          prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
          title: 'Birthday gift',
          subtitle: '<span class="text-primary">Trevor Hansen</span> &mdash; Have any ideas about what we should get Heidi for her birthday?',
        },
        { type: 'divider', inset: true },
        {
          prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
          title: 'Recipe to try',
          subtitle: '<span class="text-primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash, Corn, and tomatillo Tacos.',
        },
      ],
      posts: []
    };
  },
  computed: {
    ...mapState('user', ['token'])
  },
  mounted() {
    this.getPosts();
  },
  methods: {
    ...mapActions('notification', ['acSetError']),
    async getPosts() {
      const res = await request.get("/posts/list");
      console.log("new posts: ", res.data);
      if (STATUS_OK === res.data.code) {
        this.posts = res.data.data.posts;
        console.log("posts: ", this.posts);
      } else {
        this.acSetError(res.data.msg);
      }
    },
    formatDate(val) {
      return formattedDate(val)
    },
    addForum() {
      this.$router.push("/forum/add");
    },
    goDetail(id) {
      this.$router.push(`/posts/${id}`);
    }
  }
}
</script>

<style scoped>
  .forum-container {
    height: 100%;
  }
</style>