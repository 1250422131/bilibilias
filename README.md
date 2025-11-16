<div align="center">

# BILIBILIAS

![bilibilias](https://socialify.git.ci/1250422131/bilibilias/image?custom_description=%E4%BE%BF%E6%8D%B7%E7%9A%84%E7%BC%93%E5%AD%98B%E7%AB%99%E8%A7%86%E9%A2%91%E5%92%8C%E7%95%AA%E5%89%A7&description=1&forks=1&language=1&logo=https%3A%2F%2Fi0.hdslb.com%2Fbfs%2Fim_new%2Fda04832e1000648bef279aa2b2b73a52351201307.png&name=1&owner=1&pattern=Circuit+Board&stargazers=1&theme=Light)



</div>

---

## 简介

BILIBILIAS是一款第三方的B站（哔哩哔哩）视频缓存工具，旨在帮助需要离线播放或者剪辑原创视频的自媒体博主。

UP主可以下载一些剪辑素材来制作做视频，学生群体可缓存自己需要的教学视频在其他设备观看。

## 特别的

> [!WARNING]
> 在本项目缓存的任何内容都不得直接进行二次传播，仅允许在您自己的终端设备播放或者制作剪辑视频（未经作者允许不得直接搬运）。
>
> 您必须合理的使用本项目，请勿用于任何商业/非法用途，否则一切后果将由您自己承担，BILIBILIAS项目成员不会承担任何责任。

BILIBILIAS 是由开源社区开发的 B 站外置工具:

BILIBILIAS没有得到哔哩哔哩许可，哔哩哔哩对用户使用此软件而产生的一切后果概不负责。

BILIBILIAS是一款技术学习驱动的开源项目，我们很多的经验都来自本项目，同时也意味着该项目随时可能停止维护！

## 立场

BILIBILIAS 依托于B站开发，尊重并支持哔哩哔哩的各项规则和政策，任何使用本项目的用户都必须遵守哔哩哔哩的相关规定。

我们希望为B站正常用户提供便利的同时，尊重并保护哔哩哔哩及其内容创作者的合法权益，项目也在积极的探索版权保护和合理使用的平衡点，并积极研制对本项目下载内容的版权保护措施。

## 宗旨

分享快乐，传递价值！

## 特征

- 登录
    - [x] 扫码登录
    - [x] Cookie登录
- 支持解析类型
    - [x] 普通视频
    - [x] 番剧、影视视频
    - [x] 充电视频
    - [x] 互动视频
    - [ ] 课堂（涉及权益问题，暂时不会支持）
- 支持解析方式
    - [x] AV、BV、EP、SS链接
    - [x] B站分享到APP内解析
    - [x] 投稿、收藏夹、最近观看、点赞、追番列表进行解析
    - [x] 内置网页解析
- 下载工具
    - [x] 内置下载器
    - [ ] Aria2
- 辅助工具
    - [x] 视频逐帧导出
- 漫游支持
    - [x] 内置漫游服务器
    - [ ] 自定义漫游服务器
- 字幕下载
    - [x] ASS字幕
    - [x] SRT字幕

## 路线

当前路线尚未补充，后续会进行完善。

- [x] 版本 3.1.0
    - [x] 2.x基础功能实现
- [ ] 版本 3.2.0
    - [ ] 下载完成列表排序
    - [ ] 缓存代码进一步封装
    - [ ] 完善平板UI适配
    - [ ] APP使用文档

## BILIBILIAS-FFmpeg

本项目所采用的[FFmpeg](https://github.com/FFmpeg/FFmpeg/)
由[BILIBILIAS-FFmpeg](https://github.com/1250422131/bilibilias-ffmpeg)进行构建，完全使用FFmpeg的源代码。

### 下载与配置

在运行项目之前，你需要先前往仓库下载构建产物，这是因为二进制文件很大，不应该被用作为Git仓库的一部分，所以我们使用GitHub
Releases来托管它们。

请前往[BILIBILIAS-FFmpeg Releases](https://github.com/1250422131/bilibilias-ffmpeg/releases)下载最新版本，
并将其解压到`BILIBILIAS\core\ffmpeg\src\main\cpp`目录下。

## 设计

感谢以下设计师对本项目带来无与伦比的UI和Logo设计：

- [@123Duo3](https://github.com/123Duo3) - UI设计
- [@Jessie](https://jmkd.xyz/) - UI设计
- [@daidr](https://github.com/daidr) - Logo设计

## 鸣谢

本项目的CDN加速和安全防护由腾讯 **[EdgeOne](https://edgeone.ai/?from=github)** 支持！

[<img src="https://edgeone.ai/_next/static/media/headLogo.daeb48ad.png?auto=format&fit=max&w=64"
alt="EdgeOne"
height="60">](https://edgeone.ai/?from=github)