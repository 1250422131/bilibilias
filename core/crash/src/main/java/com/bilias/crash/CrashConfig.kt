package com.bilias.crash

import com.bilias.crash.filter.CrashReportFilter
import com.bilias.crash.filter.DefaultFilter
import com.bilias.crash.policy.CrashReportPolicy
import com.bilias.crash.policy.StoragePolicy

/**
 * 全局崩溃信息收集的配置，也包括部分日志信息收集的配置
 */
class CrashConfig(builder: Builder) {
    var policy: CrashReportPolicy
        private set

    // 决定是否要上报该次崩溃信息
    var filter: CrashReportFilter
        private set

    // 包含崩溃信息内容
    var info: AppInfo
        private set

    // 是否开启崩溃日志收集功能
    var enabled: Boolean
        private set

    // 收集崩溃信息后，是否让应用闪退，true则不闪退
    var interceptCrash: Boolean
        private set

    // 是否初始化日志通知服务
    var initLogNotification: Boolean
        private set

    init {
        policy = builder.policy
        filter = builder.filter
        info = builder.appInfo
        enabled = builder.enabled
        interceptCrash = builder.interceptCrash
        initLogNotification = builder.initLogNotification
    }

    class Builder(info: AppInfo) {
        var policy: CrashReportPolicy = StoragePolicy()
        var filter: CrashReportFilter = DefaultFilter()
        var enabled = true
        var interceptCrash = false
        var initLogNotification = false
        var appInfo: AppInfo = info

        fun crashReportPolicy(policy: CrashReportPolicy): Builder = apply {
            this.policy = policy
        }

        fun filterChain(filter: CrashReportFilter): Builder = apply {
            this.filter = filter
        }

        fun enabled(enabled: Boolean): Builder = apply {
            this.enabled = enabled
        }

        fun interceptCrash(interceptCrash: Boolean): Builder = apply {
            this.interceptCrash = interceptCrash
        }

        fun initLogNotification(initLogNotification: Boolean): Builder = apply {
            this.initLogNotification = initLogNotification
        }

        fun build(): CrashConfig {
            val config = CrashConfig(this)
            BiliASUncaughtExceptionHandler.getInstance(config)
            return config
        }
    }
}