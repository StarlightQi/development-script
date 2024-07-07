<template>
  <div>
    测试
  </div>
</template>


<script lang="ts" setup>
// ElMessage.error('插件加载成功');
// Example of sending data from a web page to the background script
const data = window.localStorage.getItem("TestData");
if (data) {
  console.log("浏览器插件数据载入成功！")
  console.log(JSON.parse(data))
}
</script>
