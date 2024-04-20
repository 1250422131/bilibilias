package com.imcys.bilibilias.core.download.extension

import com.imcys.bilibilias.core.download.DownloadProgress
import com.liulishuo.okdownload.DownloadTask
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Correspond to [DownloadTask.enqueue].
 * This method will create a [com.liulishuo.okdownload.core.listener.DownloadListener1]
 * instance internally.
 */
fun DownloadTask.enqueue(
    taskStart: onTaskStartWithModel? = null,
    retry: onRetry? = null,
    connected: onConnected? = null,
    progress: onProgress? = null,
    taskEnd: onTaskEndWithModel? = null
) = enqueue(createListener1(taskStart, retry, connected, progress, taskEnd))

fun DownloadTask.spChannel(): Channel<DownloadProgress> {
    val channel = Channel<DownloadProgress>(Channel.CONFLATED)
    val channelClosed = AtomicBoolean(false)
    createListener1(
        progress = { task, currentOffset, totalLength ->
            if (channelClosed.get()) return@createListener1
            channel.trySend(DownloadProgress(task, currentOffset, totalLength))
        }
    ) { _, _, _, _ ->
        channelClosed.set(true)
        channel.close()
    }.also { it.setAlwaysRecoverAssistModelIfNotSet(true) }
    return channel
}

internal fun CancellableContinuation<*>.cancelDownloadOnCancellation(task: DownloadTask) =
    invokeOnCancellation { task.cancel() }
