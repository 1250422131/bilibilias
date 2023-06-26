import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "BILIBILIAS",
  description: "BILIBILIAS使用和开发文档",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: '首页', link: '/' },
      { text: '使用文档', link: '/guide/as-documentation.md' },
      {
        text: "2.0.31",
        items: [
          {
            text: 'Changelog',
            link: 'https://github.com/vuejs/vitepress/blob/main/CHANGELOG.md'
          },
          {
            text: 'Contributing',
            link: 'https://github.com/vuejs/vitepress/blob/main/.github/contributing.md'
          }
        ]
      }
    ],

    sidebar: [
      {
        text: '使用文档',
        items: [
          { text: '开始', link: '/guide/as-documentation.md' },
        ]
      },
      {
        text: '开发文档',
        items: [
          { text: '开始', link: '/guide/as-dev-documentation-star.md' },
          { text: '组件化路由', link: '/guide/as-dev-documentation-arouter.md' },
          { text: '内置播放器', link: '/guide/as-dev-documentation-play.md' },

        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/1250422131/bilibilias' }
    ]
  },
  ignoreDeadLinks: true
})
