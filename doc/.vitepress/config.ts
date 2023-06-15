import { defineConfig } from 'vitepress'

export default defineConfig({
  title: "BILIBILIAS",
  description: "BILIBILIAS使用和开发文档",
  locales: [
    {
      code: 'zh-CN',
      name: '简体中文',
      description: '简体中文',
    },
    {
      code: 'en-US',
      name: 'English',
      description: 'English',
    },
  ],
  defaultLocale: 'zh-CN',
  themeConfig: {
    nav: [
      { text: '首页', link: '/zh-CN/' },
      { text: '使用文档', link: '/zh-CN/guide/as-documentation.md' },
      {
        text: "2.0.31",
        items: [
          {
            text: '更新日志',
            link: 'https://github.com/vuejs/vitepress/blob/main/CHANGELOG.md'
          },
          {
            text: '贡献指南',
            link: 'https://github.com/vuejs/vitepress/blob/main/.github/contributing.md'
          }
        ]
      },
      {
        text: 'English',
        link: '/en-US/',
      },
    ],
    sidebar: {
      '/zh-CN/guide/': [
        {
          text: '使用文档',
          children: [
            { text: '开始', link: '/zh-CN/guide/as-documentation.md' },
          ]
        },
        {
          text: '开发文档',
          children: [
            { text: '开始', link: '/zh-CN/guide/as-dev-documentation-star.md' },
            { text: '组件化路由', link: '/zh-CN/guide/as-dev-documentation-arouter.md' },
            { text: '内置播放器', link: '/zh-CN/guide/as-dev-documentation-play.md' },
          ]
        },
      ],
      '/en-US/guide/': [
        {
          text: 'Documentation',
          children: [
            { text: 'Getting Started', link: '/en-US/guide/as-documentation.md' },
          ]
        },
        {
          text: 'Development',
          children: [
            { text: 'Getting Started', link: '/en-US/guide/as-dev-documentation-star.md' },
            { text: 'Component-based Routing', link: '/en-US/guide/as-dev-documentation-arouter.md' },
            { text: 'Built-in Player', link: '/en-US/guide/as-dev-documentation-play.md' },
          ]
        },
      ],
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/1250422131/bilibilias' }
    ]
  },
  ignoreDeadLinks: true,
})
