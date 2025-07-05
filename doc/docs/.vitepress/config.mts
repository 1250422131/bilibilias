import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  lang: 'zh-CN',
  title: "BILIBILIAS-DOC",
  description: "BILIBILIAS的使用文档与开发文档。",
  lastUpdated: true,
  head: [
    ['link', { rel: "icon", href: "/favicon.ico" }]
  ],
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: '主页', link: '/' },
      { text: '用户手册', link: '/user/introduction', activeMatch: '/user/' },
      {
        text: '2.1.5',
        items: [
          { text: '贡献', link: 'https://github.com/1250422131/bilibilias' },
        ]
      }
    ],

    sidebar: [
      {
        text: '简介',
        items: [
          { text: 'BILIBILIAS是什么？', link: '/user/introduction' },
          { text: '开始', link: '/user/getting-started' },
        ]
      },
      {
        text: '用户手册',
        items: [
          { text: '从V1.1.5≤迁移', link: '/user/cookbook/migration' },
          { text: '长按快速下载', link: '/user/cookbook/quick-download' },
          { text: 'UP主页解析', link: '/user/cookbook/up-page-as' },
          { text: 'UP视频批量缓存', link: '/user/cookbook/up-video-batch-cache' },
          { text: '网页解析', link: '/user/cookbook/web-as' },
          { text: '更多解析方式', link: '/user/cookbook/more-as-model' },
          { text: '程序设置', link: '/user/cookbook/settings-about' },
          { text: '常见问题', link: '/user/cookbook/common-problem' },
        ]
      },
      {
        text: '关于',
        items: [
          { text: '投诉', link: '/about/complaint' },
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/1250422131/bilibilias' }
    ],
    search: {
      provider: 'local'
    },
  }
})
