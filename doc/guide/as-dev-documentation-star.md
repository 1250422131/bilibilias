# 开始

BILIBILIAS拥抱开源、开放，在GPL-3.0 license规则下我们欢迎其他开发者有自己的想法，对BILIBILIAS进行改造和二次开发。


## 拉取源代码

### 必要条件
- 自1.1.5版本开始，bilibilias重构改用[**Kotlin**](https://kotlinlang.org/)编写，因此你需要了解一些Kotlin。
- 请确保[**Java JDK**](https://www.injdk.cn/)在**11**及以上版本，**不支持Java8**

**请确保电脑上已经安装了Git**

下面几个版本稍微有些区别

- [正式版](https://github.com/1250422131/bilibilias)：已经在上线使用的版本代码
- [测试版](https://github.com/1250422131/bilibilias/tree/develop_imcys)：未正式上线，但已经发布测试的版本代码
- [开发板](https://github.com/1250422131/bilibilias/tree/develop_beta)：未上线，只通过了简单开发测试的版本代码

::: code-group

```sh [正式版]
$ git clone -b develop https://github.com/1250422131/bilibilias.git
```

```sh [测试版]
$ git clone -b develop_beta https://github.com/1250422131/bilibilias.git
```

```sh [开发版]
$ git clone -b develop_imcys https://github.com/1250422131/bilibilias.git
```
:::tip Github可能在某些地区无法访问
因为一些原因Github在部分地区是无法访问的，你需要考虑使用镜像站或者其他方式拉取。

:::
因此，建议拉取正式版代码，如果想要获得更多能力代码，就拉取测试版，最激进的开发版并不建议拉取。


## 目录结构

BILIBILIAS目前使用[KComponent](https://github.com/xiaojinzi123/KComponent)进行组件化开发，因此项目文件夹会稍微复杂一些。

emmm，比较难搞，因为目录结构太多，一次性粘贴过来也说不了什么，我们还是粘贴下大致内容吧
```
.
├─ app
├─ common
├─ model_ffmpeg
├─ tool_livestream
└─ tool_log_export 
```

其中`app`模块为BILIBILIAS的主要内容，这是因为早期并未采用组件化，导致部分核心业务没有抽离到其他模块开发。
`common`模块承担了其他模块公共的业务和依赖，因此其他所有模块必须依赖此模块。
`model_ffmpeg`模块承担了视频流处理的功能业务，有需要操作视频的模块可以依赖此模块，此模块内简单封装了部分操作。
`tool_livestream`模块是一个要被废除的模块，原本承担了直播流缓存解析的功能，后来因为需要考虑的问题较多，便不再维护。
`tool_log_export`模块是一个日志导出模块，此模块后续的设计是支持导出更多B站日志记录，如播放记录，收藏记录，硬币记录等。

当然，内部的结构仍然复杂，我们后面会介绍到。



## 配置文件

配置文件目前以app模块为主，换句话说，打包后的名称，包名等信息会以下面的信息为准，因此你可以在这里配置你自己的包名和版本信息等内容。

```gradle
android {

    defaultConfig {
        applicationId "com.imcys.bilibilias"
        minSdk 21
        //noinspecton ExpiredTargetSdkVersion
        targetSdk 29
        versionCode 203
        versionName "2.0.31"
        ......
    }

    .........
    buildTypes {


        debug {
            ......
            resValue "string", "app_name", "@string/app_name_debug"
            resValue "string", "app_channel", "@string/app_channel_debug"
        }

        release {
            ......
            resValue "string", "app_name", "@string/app_name_release"
            resValue "string", "app_channel", "@string/app_channel_release"

        }

    }
..
}
```
