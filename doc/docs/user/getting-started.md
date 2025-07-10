# 快速开始

## 安装

目前 BILIBILIAS 仅支持 Android 平台，你用你喜欢的方式下载安装包。

- [Misakamoe 官网 - 推荐安装渠道](https://api.misakamoe.com/app/)
- [Github - Releases](https://github.com/1250422131/bilibilias/releases)

官网和 Releases 发布的安装包是一致的，不过官网有时候会发布 Beta 测试版本，具体情况需要在网页查看。

此外，BILIBILIAS 已经在 F-Droid 发布，在这里的版本没有百度统计和微软统计，由 [linsui](https://github.com/linsui) 提供该平台的打包脚本与上架处理，详情见 [issues-392](https://github.com/1250422131/bilibilias/issues/392)，同时感谢 linsui 的付出。
[<AsImage  width="40%"  src="https://fdroid.gitlab.io/artwork/badge/get-it-on-zh-hans.png" :isAmplify=false></AsImage>](https://f-droid.org/packages/com.imcys.bilibilias/)

## 启动程序

首次启动 APP，如果你的设备是 Android13 及以上，那么你将会看到这样的提示，并且 APP 不会进入主页，这是因为目前 BILIBILIAS 为大家提供了自定义存储路径的功能，在高版本安卓处理自定义存储路径相当复杂，这里我们只能通过获取更高权限来解决问题，如果不授权会导致无法自定义路径。

::: tip
目前 BILIBILIAS 有计划在 Google Play 上架，上架后的版本可能不存在这个权限，当然，大概率到时间也没办法自定义存储路径。
:::

如果你目前的安卓版本需要所有文件访问权限，那么会自动收到一个申请存储权限的通知，正常放行即可。

<AsImage  width="40%"  src="/images/user/getting-started/app-start-request-permission.jpg"></AsImage>

## 解析视频

BILIBILIAS 支持多种缓存方案，如复制分享文本、哔哩哔哩直接分享给 BILIBILIAS、输入 BV 号、输入 AV 号、输入 EP 号、输入用户 UID......等方案。
下面我将介绍一些常用方式：

### 分享文本解析

分享文本解析是最简单的做法，你只需在视频页面点击分享，如下图所示，点击复制链接即可，接下来你需要将复制的链接粘贴到工具页面的输入框中，随后 BILIBILIAS 会自动解析内容。

<div class="sm:flex gap-4">
<AsImage  width="40%"  src="/images/user/getting-started/bili-link-shared.png"></AsImage>
<AsImage  width="40%"  src="/images/user/getting-started/tools-bili-link-as.jpg"></AsImage>
</div>

### 直接分享软件

直接将视频分享给 BILIBILIAS 是最快速的做法，你无需复制任何内容，点击分享给 BILIBILIAS 即可，注意如果你刚刚打开 APP，那么第一次接受分享的速度可能会稍微慢一些，耐心等待。

不过因为每个手机对分享 APP 的排名方式可能有些许差异，所以这只是理论上最快速的方案。

<div class="sm:flex gap-4">
<AsImage  width="40%"  src="/images/user/getting-started/bili-more-shared.png"></AsImage>
<AsImage  width="40%"  src="/images/user/getting-started/tools-bili-app-shared-as.png"></AsImage>
</div>

## 缓存视频

如果你已经完成了上一步[解析视频](./getting-started.html#解析视频),那么你只需要点击解析视频中的视频卡片即可进入下载页面。

如图，如果你不熟悉这些内容，建议选择默认内容即可，因为我们没有登录，所以这里最高的清晰的是 480P，如果想要更多的分辨率需要[登录账户](./getting-started.html#账户登录)。

注意，多子集的视频需要在这里选择子集，弹窗默认选择了第一集。

<div class="sm:flex gap-4">
<AsImage  width="30%"  src="/images/user/getting-started/as-video-page.jpg"></AsImage>
<AsImage  width="30%"  src="/images/user/getting-started/as-video-donwload-dialog.png"></AsImage>
<AsImage  width="30%"  src="/images/user/getting-started/download-list.jpg"></AsImage>
</div>

## 账户登录

::: warning
如果你使用的版本在 2.1.0 及以前，那么会强制要求登录，自 2.1.5 版本开始，BILBIILIAS 支持不登录使用。
:::

### 可选登录 <Badge type="tip" text="^2.1.5" /> <Badge type="warning" text="beta" />

如果你正在使用 V2.1.5 及以上版本，那么你需要在用户页面进行登录，如果是 V2.1.0 及以下版本打开 APP 则会提示强制登录，点击登录之后会弹出登录模式选择对话框，这里请大家选择 **BILIBILIAS 扫码**，云端登录暂未开放。

请大家点击**下载二维码**或者**截图当前页面**，然后点击**跳转扫码**，接下来你会看到扫码界面,如图，点击相册，选择自动保存的二维码或者手动保存的二维码进行扫码，扫码后会出现第二张图的提示，此时点击确定即可完成登录的第一步。

<div class="sm:flex gap-4">
<AsImage  width="30%"  src="/images/user/getting-started/user-not-login.jpg"></AsImage>
<AsImage  width="30%"  src="/images/user/getting-started/user-login-model-dialog.jpg"></AsImage>
<AsImage  width="30%"  src="/images/user/getting-started/user-login-qr-login.jpg"></AsImage>
</div>

当完成上一步之后即可返回 BILBIILIAS，点击完成登录按钮，此时如果正常登录了，那么就会显示用户信息对话框，如果登录有问题则会提示登录失败原因，此时可重新登录，或者点击二维码刷新后重新扫码登录。

<div class="sm:flex gap-4">
<AsImage  width="40%"  src="/images/user/getting-started/user-login-check-dialog.png"></AsImage>
<AsImage  width="40%"  src="/images/user/getting-started/user-info-dialog.png"></AsImage>
</div>

## 首页须知

你将在首页看到这些板块

- [轮播图](./getting-started.html#轮播图)
- [更新内容](./getting-started.html#更新内容)
- [致敬](./getting-started.html#致敬)
- [捐款](./getting-started.html#捐款)
- [前往反馈](./getting-started.html#前往反馈)

<AsImage  width="200px"  src="/images/user/getting-started/app-home.jpg"></AsImage>

### 轮播图

轮播图的作用是通知一些较为紧急，但不是最重要的通知，同时这里也是 BILIBILIAS 文化输出的载体，BILIBILIAS 会在这里宣发一些符合价值观的视频或者内容。

### 更新内容

首页并没有直接让大家下载视频，是因为有比这个更加重要的事情，那就是每个版本的更新说明，由于 BILIBILIAS 开发较为早期，有许多隐患问题，且每次更新内容也比较多，我们特意留了首页的更新内容，方便大家查看。

尽管如此，还是只有少部分用户会点击进去关注这些更新内容，所以我们呼吁大家每个版本看一下。

### 致敬

BILIBILIAS 是一款开源项目，从 2019 年到现在已经很多年了，之所以更新这么久是因为有许多社区成员的支持，虽然致敬模块有一段时间没有维护，但是你仍然可以在这里看到早期对 BILIBILIAS 做出杰出贡献的社区成员。

### 捐款

BILIBILIAS 有一些服务器在运作，这些服务器主要是帮助我们统计数据和发布通知，此外我们还需要服务器搭建官网，方便各位用户进行下载。
这些服务器的运作需要不少开销，因此我们设立了捐款模块，方便大家为我们进行捐助，我们也会用资金去升级一些服务。

### 前往反馈

::: warning
早期 BILIBILIAS 一直使用 [腾讯兔小巢](https://support.qq.com/product/337496),后来因为腾讯兔小巢转为收费，我们只能迁移到 QQ 频道，详细见：[BILIBILIAS 重要变更通知](https://pd.qq.com/s/57dh2bcoz)。
:::

BILIBILIAS 目前的版本开发时间较为久远，导致有非常多的 BUG，因此为大家提供了反馈的社区，目前在 [BILIBILIAS-QQ 频道](https://pd.qq.com/s/15f4xoaz8)。
大家可以在 QQ 频道里反馈问题和参与测试版本的测试，测试版本作为抢先版本，可以先体验到开发的新功能，我们也需要大家参与测试反馈问题。
所以也欢迎大家来这里测试。
