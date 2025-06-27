import DefaultTheme from 'vitepress/theme'
import './style/index.css'
import AsImage from './components/AsImage.vue'

export default {
    extends: DefaultTheme,
    enhanceApp({ app, router, siteData }) {
        app.component('AsImage', AsImage)
    },
}