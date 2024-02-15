package com.imcys.datastore.fastkv

import android.content.*
import dagger.hilt.android.qualifiers.*
import javax.inject.*

/**
 * APP使用信息
 */
@Singleton
class UsageData @Inject constructor(@ApplicationContext context: Context) :
    FastKVOwner("common_storage", context) {
    var launchCount by int()

    // 首次启动时间
    var firstLaunchTime by long()

    var lastLaunchTime by long()

    // 首次安装的渠道
    var firstChannel by string()

    // 上次安装版本（用于判断本次打开是否版本升级）
    var lastVersion by int()

    var benchmarkCount by int()
}
