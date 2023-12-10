package com.bilias.crash.filter

/** 组合过滤器 */
class CrashReportFilterChain {
    private val filters = mutableListOf<CrashReportFilter>()
    fun addFirst(filter: CrashReportFilter): CrashReportFilterChain = apply {
        filters.add(0, filter)
    }

    fun addLast(filter: CrashReportFilter): CrashReportFilterChain = apply {
        filters.add(filter)
    }

    val filter: CrashReportFilter?
        get() {
            var first: CrashReportFilter? = null
            var last: CrashReportFilter? = null
            for (i in filters.indices) {
                val filter = filters[i]
                if (i == 0) {
                    first = filter
                } else {
                    last?.setNextFilter(filter)
                }
                last = filter
            }
            return first
        }
}