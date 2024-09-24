<template>  
  <v-app>  
    <v-snackbar  
      v-model="showSnackbar"  
      :timeout="3000"  
      :text="errorMessage"  
      @input="closeSnackbar"  
    ></v-snackbar>  
  </v-app>
</template>  
  
<script>  
import { mapState, mapActions } from 'vuex';  
  
export default {  
  computed: {  
    ...mapState('notification', ['errorMessage']),  
    showSnackbar: {  
      get() {
        return this.errorMessage !== '';  
      },  
      set(value) {  
        if (!value) {  
          this.acClearError();  
        }  
      }  
    }  
  },  
  methods: {
    ...mapActions("notification", ['acSetError', 'acClearError']),
    closeSnackbar() {  
      this.showSnackbar = false;  
    }  
  }  
};  
</script>