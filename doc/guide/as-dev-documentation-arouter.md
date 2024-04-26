
# 组件化路由

组件化由于我们使用了[KComponent](https://github.com/xiaojinzi123/KComponent)，因此路由配置可以从这个库了解到。

但是我们的路由地址被集中管理了，因此你可以在**common**模块下的`com.imcys.bilibilias.common.base.arouter.ARouterAddress`中找到全部的路由地址。

[ARouterAddress](https://github.com/1250422131/bilibilias/blob/45a18de2d405a04e62952957e489a39852e4272c/common/src/main/java/com/imcys/bilibilias/common/base/arouter/ARouterAddress.kt)，我们推荐后续开发将路由继续添加在这个类，以免后期修改路由麻烦。

```kotlin

object ARouterAddress : Serializable {

    const val AppHomeActivity = "app/HomeActivity"

    const val BangumiFollowActivity = "app/BangumiFollowActivity"

    const val ToolFragment = "app/ToolFragment"

    const val AppHomeFragment = "app/HomeFragment"

    const val RoamMainActivity = "tool_roam/RoamMainActivity"

    const val CreateRoamActivity = "tool_roam/CreateRoamActivity"

    const val LiveStreamActivity = "tool_livestream/LiveStreamActivity"

    const val LogExportActivity = "tool_log_export/LiveStreamActivity"

}
```
