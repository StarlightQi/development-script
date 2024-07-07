import './script/HookRequest'
import { createApp } from 'vue';
import App from './App.vue';
const app = createApp(App);
import 'element-plus/dist/index.css'
const mainId="myapptest";
if (!document.getElementById(mainId)){
    const div=document.createElement("div");
    div.id=mainId;
    document.body.appendChild(div);
}
app.mount('#myapptest');
