import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import PrimeVue from 'primevue/config';
import 'primeicons/primeicons.css';
import './assets/styles.css';

import { MyPreset } from './theme.js'

const app = createApp(App)

app.use(router)

app.use(PrimeVue, {
    theme: {
        preset: MyPreset
    }
});

app.mount('#app')
