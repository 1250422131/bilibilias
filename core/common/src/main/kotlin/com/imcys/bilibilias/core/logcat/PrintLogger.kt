package com.imcys.bilibilias.core.logcat

/**
 * A [LogcatLogger] that always logs and delegates to [println] concatenating
 * the tag and message, separated by a space. Alternative to [AndroidLogcatLogger]
 * when running on a JVM.
 */
object PrintLogger : LogcatLogger {

    override fun log(priority: LogPriority, tag: String, message: String) {
        println("$tag $message")
    }
}
