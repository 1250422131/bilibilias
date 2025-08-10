package com.imcys.bilibilias.core.logging

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import kotlin.time.Clock

class FileLogWriter(
    private val config: FileLogWriterConfig,
    private val clock: Clock = Clock.System,
    private val messageStringFormatter: MessageStringFormatter = DefaultFormatter,
    private val fileSystem: FileSystem = SystemFileSystem,
) : LogWriter() {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val coroutineScope = CoroutineScope(
        newSingleThreadContext("RollingFileLogWriter") +
                SupervisorJob() +
                CoroutineName("RollingFileLogWriter") +
                CoroutineExceptionHandler { _, throwable ->
                    // can't log it, we're the logger -- print to standard error
                    println("RollingFileLogWriter: Uncaught exception in writer coroutine")
                    throwable.printStackTrace()
                },
    )

    private val loggingChannel: Channel<Buffer> = Channel()

    init {
        coroutineScope.launch {
            writer()
        }
    }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        bufferLog(
            formatMessage(
                severity = severity,
                tag = Tag(tag),
                message = Message(message),
            ),
            throwable,
        )
    }

    private fun bufferLog(message: String, throwable: Throwable?) {
        val log = buildString {
            append(clock.now().format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET))
            append(" ")
            appendLine(message)
            if (throwable != null) {
                appendLine(throwable.stackTraceToString())
            }
        }
        loggingChannel.trySendBlocking(Buffer().apply { writeString(log) })
    }

    private fun formatMessage(severity: Severity, tag: Tag?, message: Message): String =
        messageStringFormatter.formatMessage(severity, if (config.logTag) tag else null, message)

    private fun shouldRollLogs(logFilePath: Path): Boolean {
        val size = fileSizeOrZero(logFilePath)
        return size > config.rollOnSize
    }

    private fun rollLogs() {
        if (fileSystem.exists(pathForLogIndex(config.maxLogFiles - 1))) {
            fileSystem.delete(pathForLogIndex(config.maxLogFiles - 1))
        }
        (0..<(config.maxLogFiles - 1)).reversed().forEach {
            val sourcePath = pathForLogIndex(it)
            val targetPath = pathForLogIndex(it + 1)
            if (fileSystem.exists(sourcePath)) {
                try {
                    fileSystem.atomicMove(sourcePath, targetPath)
                } catch (e: IOException) {
                    // we can't log it, we're the logger -- print to standard error
                    println(
                        "RollingFileLogWriter: Failed to roll log file $sourcePath to $targetPath (sourcePath exists=${
                            fileSystem.exists(
                                sourcePath,
                            )
                        })",
                    )
                    e.printStackTrace()
                }
            }
        }
    }

    private fun pathForLogIndex(index: Int): Path =
        Path(
            config.logFilePath,
            if (index == 0) "${config.logFileName}.log" else "${config.logFileName}-$index.log"
        )

    private suspend fun writer() {
        val logFilePath = pathForLogIndex(0)

        if (fileSystem.exists(logFilePath) && shouldRollLogs(logFilePath)) {
            rollLogs()
        }

        fun createNewLogSink(): Sink = fileSystem
            .sink(logFilePath, append = true)
            .buffered()

        var currentLogSink: Sink = createNewLogSink()

        while (currentCoroutineContext().isActive) {
            // wait for data to be available, flush periodically
            val result = loggingChannel.receiveCatching()

            // check if logs need rolling
            if (shouldRollLogs(logFilePath)) {
                currentLogSink.close()
                rollLogs()
                currentLogSink = createNewLogSink()
            }

            result.getOrNull()?.transferTo(currentLogSink)

            // we could improve performance by flushing less frequently at the cost of potential data loss,
            // but this is a safe default
            currentLogSink.flush()
        }
    }

    private fun fileSizeOrZero(path: Path) = fileSystem.metadataOrNull(path)?.size ?: 0
}

data class FileLogWriterConfig(
    val logFileName: String,
    val logFilePath: Path,
    val rollOnSize: Long = 10 * 1024 * 1024, // 10MB
    val maxLogFiles: Int = 5,
    val logTag: Boolean = true,
    val prependTimestamp: Boolean = true,
)