package com.bilias.crash

import android.os.Process
import kotlin.system.exitProcess

/** 拦截应用抛出的所有异常 */
internal class BiliASUncaughtExceptionHandler private constructor(
    private val config: CrashConfig,
    private var mDefaultExceptionHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
) : Thread.UncaughtExceptionHandler {
    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (config.enabled) {
            val filterResult = config.filter.filterCrashInfo(config.info)
            if (filterResult) {
                interceptException(t, e)
            }
        }
        if (!config.interceptCrash) {
            // 如果系统提供了异常处理类，则交给系统去处理
            if (mDefaultExceptionHandler != null) {
                mDefaultExceptionHandler?.uncaughtException(t, e)
            } else {
                // 否则我们自己处理，自己处理通常是让app退出
                Process.killProcess(Process.myPid())
                exitProcess(0)
            }
        }
    }

    private fun interceptException(t: Thread, e: Throwable) {
        // 收集异常信息，用于处理
        val collector = CrashCollector()
        val info = config.info
        val crashInfo = CrashInfo(t, e, info)
        collector.collect(crashInfo)
        collector.report(config.policy)
    }

    companion object {
        fun getInstance(config: CrashConfig): BiliASUncaughtExceptionHandler {
            val handler by lazy {
                BiliASUncaughtExceptionHandler(config)
            }
            return handler
        }
    }
}