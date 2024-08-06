package com.imcys.bilibilias.core.logcat

class TestLogcatLogger(private val isLoggable: (LogPriority) -> Boolean = { true }) : LogcatLogger {
    override fun isLoggable(priority: LogPriority): Boolean = isLoggable.invoke(priority)

    data class Log(
        val priority: LogPriority,
        val tag: String,
        val message: String,
    )

    var latestLog: Log? = null

    override fun log(
        priority: LogPriority,
        tag: String,
        message: String,
    ) {
        latestLog = Log(priority, tag, message)
    }
}