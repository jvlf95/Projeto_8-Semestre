<template>
  <router-view/>
</template>

<script>
  import navComp from "./components/nav.vue"
  export default {
    components:{
      navComp
    },
    mounted(){
      document.title = "Guardian Tech"
    }
  }
</script>

