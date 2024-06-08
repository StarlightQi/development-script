<template>
  <div>
    <h1>Hello Vue 3 + TypeScript!</h1>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',
  mounted() {
    setInterval(()=>{
      console.log("插件加载成功")
    },1000)
  }
});
</script>
