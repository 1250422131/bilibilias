package com.imcys.bilibilias.core.logcat

/**
 * An enum for log priorities that map to [android.util.Log] priority constants
 * without a direct import.
 */
enum class LogPriority(
    val priorityInt: Int,
) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7),
}
