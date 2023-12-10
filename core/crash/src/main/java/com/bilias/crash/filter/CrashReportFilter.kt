package com.bilias.crash.filter

import com.bilias.crash.Info

/** 影响崩溃信息是否会上报 */
abstract class CrashReportFilter {
    private lateinit var mNext: CrashReportFilter
    fun setNextFilter(filter: CrashReportFilter) {
        mNext = filter
    }

    fun nextFilter(): CrashReportFilter {
        return mNext
    }

    fun filterCrashInfo(info: Info): Boolean {
        val result = handle(info)
        return if (result && ::mNext.isInitialized) {
            mNext.filterCrashInfo(info)
        } else {
            result
        }
    }

    abstract fun handle(info: Info): Boolean
}