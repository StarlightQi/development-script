<template>
  <div>
  </div>
</template>


<script lang="ts">
import {defineComponent} from 'vue';
import {ElMessage} from 'element-plus'

export default defineComponent({
  name: 'App',
  components: {},
  mounted() {
    ElMessage.error('插件加载成功');
  },
  methods: {}
});
</script>
