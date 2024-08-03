package com.bilias.crash

import com.bilias.crash.group.Group
import com.bilias.crash.policy.CrashReportPolicy

/**
 * 专门用来收集应用抛出的异常
 */
class CrashCollector : Collector<Info, CrashReportPolicy>() {
    private var appInfo: Info? = null
    override fun collect(info: Info) {
        appInfo = info
    }

    override fun report(policy: CrashReportPolicy) {
        reportCrash(policy, policy.group)
    }

    private fun reportCrash(policy: CrashReportPolicy, group: Group) {
        appInfo?.let { policy.report(it, group) }
    }
}