package com.bilias.crash.filter

import com.bilias.crash.CrashInfo
import com.bilias.crash.Info

/** 主线程过滤器。 */
class ActivityThreadFilter : CrashReportFilter() {
    override fun handle(info: Info): Boolean {
        if (info is CrashInfo) {
            return info.thread.name == "main"
        }
        return false
    }
}