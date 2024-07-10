import { createApp } from 'vue';
import App from './App.vue';
const app = createApp(App);
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
app.use(ElementPlus)
app.mount('#app');
