<template>
  <div style="width: 800px;height: 600px">
    <el-tabs
        v-model="activeName"
        type="card"
        class="demo-tabs"
        @tab-click="handleClick"
    >
      <el-tab-pane label="Config" name="second">
        <el-button ref="sendMessage" @click="test">发送消息测试</el-button>
      </el-tab-pane>
      <el-tab-pane label="Role" name="third">Role</el-tab-pane>
      <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import type {TabsPaneContext} from 'element-plus'

const activeName = ref('first')
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}

function test(){
  chrome.tabs.query({active: true, currentWindow: true}, (tabs:any) => {
    chrome.scripting.executeScript({
      target: {tabId: tabs[0].id},
      function: sendMessageToPage
    });
  });
}

function sendMessageToPage() {
  window.postMessage({type: 'FROM_POPUP', text: 'Hello from the popup!'}, '*');
}
</script>

<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
