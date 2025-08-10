package com.imcys.bilibilias.core.logging

import co.touchlab.kermit.LoggerConfig
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.imcys.bilibilias.core.context.KmpContext
import kotlinx.io.files.SystemFileSystem
import co.touchlab.kermit.Logger as KermitLogger

fun logger(tag: String): Logger {
    val log = KermitLogger(loggerConfigurationInit(), tag)
    return LoggerImpl(log)
}

inline fun <reified T : Any> logger(): Logger {
    return logger(T::class.simpleName!!)
}

interface Logger {
    public fun error(message: () -> String)
    public fun error(cause: Throwable, message: () -> String)
    public fun warn(message: () -> String)
    public fun warn(cause: Throwable, message: () -> String)
    public fun info(message: () -> String)
    public fun info(cause: Throwable, message: () -> String)
    public fun debug(message: () -> String)
    public fun debug(cause: Throwable, message: () -> String)
    public fun trace(message: () -> String)
    public fun trace(cause: Throwable, message: () -> String)
}

class LoggerImpl(private val log: KermitLogger) : Logger {
    override fun error(message: () -> String) {
        log.e(message())
    }

    override fun error(cause: Throwable, message: () -> String) {
        log.e(message(), cause)
    }

    override fun warn(message: () -> String) {
        log.w(message())
    }

    override fun warn(cause: Throwable, message: () -> String) {
        log.w(message(), cause)
    }

    override fun info(message: () -> String) {
        log.i(message())
    }

    override fun info(cause: Throwable, message: () -> String) {
        log.i(message())
    }

    override fun debug(message: () -> String) {
        log.d(message())
    }

    override fun debug(cause: Throwable, message: () -> String) {
        log.d(message(), cause)
    }

    override fun trace(message: () -> String) {
        log.a(message())
    }

    override fun trace(cause: Throwable, message: () -> String) {
        log.a(message(), cause)
    }
}

fun loggerConfigurationInit(): LoggerConfig {
    return if (KmpContext.isDebug) {
        loggerConfigInit(
            platformLogWriter(NoTagFormatter),
        )
    } else {
        SystemFileSystem.createDirectories(KmpContext.logsDir)
        loggerConfigInit(
            FileLogWriter(
                FileLogWriterConfig("app", KmpContext.logsDir),
            )
        )
    }
}