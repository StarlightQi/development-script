import { createApp } from 'vue';
import App from './App.vue';
const app = createApp(App);
import 'element-plus/dist/index.css'

const div=document.createElement("div");
div.id="myapptest";
document.body.appendChild(div);

app.mount('#myapptest');
