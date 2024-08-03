package com.bilias.crash.filter

import com.bilias.crash.Info

class DefaultFilter : CrashReportFilter() {
    override fun handle(info: Info): Boolean = true
}