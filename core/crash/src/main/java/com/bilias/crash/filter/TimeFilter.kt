package com.bilias.crash.filter

import com.bilias.crash.Info
import java.util.Calendar

/** 按时间过滤崩溃信息 */
class TimeFilter : CrashReportFilter() {
    override fun handle(info: Info): Boolean {
        // 24小时制
        val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
        return hour in 8..19
    }
}