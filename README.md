<div align="center">

# BILIBILIAS

![bilibilias](https://socialify.git.ci/1250422131/bilibilias/image?description=1&descriptionEditable=%E4%BE%BF%E6%8D%B7%E7%9A%84%E7%BC%93%E5%AD%98B%E7%AB%99%E8%A7%86%E9%A2%91%E5%92%8C%E7%95%AA%E5%89%A7&font=Inter&forks=1&language=1&logo=https%3A%2F%2Fi0.hdslb.com%2Fbfs%2Fim_new%2F18b70b81972a79923f179106c406910a351201307.png&name=1&owner=1&pattern=Circuit%20Board&stargazers=1&theme=Auto)

</div>

---

> [!CAUTION]
> 这是 BILIBILIAS 3.x 重构版本的分支，请不要使用这个分支进行构建，它仍然在开发中！！！

BILIBILIAS 近两年来，积攒的功能是非常多的，但由于早期架构薄弱，导致 2.x 在功能稳定性的维护上变得非常困难。
因此，我们准备启动 3.x 的重构计划，在保障稳定性的前提下，将 2.x 的功能和更多功能加入到 3.x 版本。

## 2.1.5 迁移任务

有兴趣参与维护的用户可以关注进展和设计：
<table>
    <thead>
        <tr>
            <th>模块</th>
            <th>任务</th>
            <th>功能说明</th>
            <th>进展</th>
            <th>分配</th>
        </tr>
    </thead>
    <tbody>
        <!-- 全局模块 -->
        <tr>
            <td rowspan="2">全局</td>
            <td>分享解析</td>
            <td>支持从 B 站分享到 App 时自动跳转并解析内容</td>
            <td>已完成</td>
            <td><a href="https://github.com/1250422131/">1250422131</a></td>
        </tr>
        <tr>
            <td>跳转解析</td>
            <td>视频/番剧列表项点击后自动跳转解析页面并解析内容</td>
            <td>已完成</td>
            <td><a href="https://github.com/1250422131/">1250422131</a></td>
        </tr>
        <!-- 首页模块 -->
        <tr>
            <td>首页</td>
            <td>网页解析</td>
            <td>参考 <a href="https://bilibilias-doc.imcys.com/user/cookbook/web-as.html">网页解析文档</a></td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <!-- 个人主页模块 -->
        <tr>
            <td rowspan="4">个人主页</td>
            <td>点赞视频列表页</td>
            <td>参考 2.1.5，需支持列表分页、排序、搜索过滤</td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <tr>
            <td>投币视频列表页</td>
            <td>参考“个人主页-点赞视频列表页”</td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <tr>
            <td>收藏视频列表页</td>
            <td>参考 2.1.5，需区分收藏夹，其他同点赞/投币列表页</td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <tr>
            <td>追番列表页</td>
            <td>参考 2.1.5，需标注番剧类型和追番进度</td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <!-- 设置模块 -->
        <tr>
            <td>设置</td>
            <td>命名规则</td>
            <td>参考 <a href="https://bilibilias-doc.imcys.com/user/cookbook/settings-about.html#%E5%91%BD%E5%90%8D%E8%A7%84%E5%88%99">命名规则文档</a>，
            优化填写方案，需支持视频和番剧两套规则，类似 <a href="https://github.com/JunkFood02/Seal/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/6.jpg">Seal-下载参数</a>，
            并且需要参考目前项目可以拿到哪些参数，才可以去写固定对应的命名规则变量。
            </td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
        <!-- 缓存模块 -->
        <tr>
            <td>缓存</td>
            <td>下载字幕</td>
            <td>参考 3.0.0 下载封面功能，勾选后展示可下载字幕类型，类型转化参考 2.1.5 代码</td>
            <td>未开始</td>
            <td>未分配</td>
        </tr>
    </tbody>
</table>

## BILIBILIAS-FFmpeg

仓库待定

## BILIBILIAS-Aria2

仓库待定

## 技术路线

技术路线待定

## 反馈渠道

企鹅群：812128563

哔哩哔哩：[萌新杰少](https://space.bilibili.com/351201307)

反馈频道：[QQ 频道](https://pd.qq.com/s/ecbbiumzr)
