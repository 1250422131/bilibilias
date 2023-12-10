package com.bilias.crash

import com.bilias.crash.policy.LogReportPolicy

/**
 * 日志信息收集器
 */
class LogCollector : Collector<LogInfo, LogReportPolicy>() {
    private var info: LogInfo? = null
    override fun collect(info: LogInfo) {
        this.info = info
    }

    override fun report(policy: LogReportPolicy) {
        info?.let { policy.report(it, policy.group) }
    }
}