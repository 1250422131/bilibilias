package com.imcys.bilibilias.core.download.extension

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist

/**
 * Correspond to [com.liulishuo.okdownload.core.listener.DownloadListener1.taskStart].
 */
typealias onTaskStartWithModel = (task: DownloadTask, model: Listener1Assist.Listener1Model) -> Unit

/**
 * Correspond to [com.liulishuo.okdownload.core.listener.DownloadListener1.retry].
 */
typealias onRetry = (task: DownloadTask, cause: ResumeFailedCause) -> Unit

/**
 * Correspond to [com.liulishuo.okdownload.core.listener.DownloadListener1.connected].
 */
typealias onConnected = (
    task: DownloadTask,
    blockCount: Int,
    currentOffset: Long,
    totalLength: Long
) -> Unit

/**
 * Correspond to [com.liulishuo.okdownload.core.listener.DownloadListener1.progress].
 */
typealias onProgress = (task: DownloadTask, currentOffset: Long, totalLength: Long) -> Unit

/**
 * Correspond to [com.liulishuo.okdownload.core.listener.DownloadListener1.taskEnd].
 */
typealias onTaskEndWithModel = (
    task: DownloadTask,
    cause: EndCause,
    realCause: Exception?,
    model: Listener1Assist.Listener1Model
) -> Unit

/**
 * A concise way to create a [DownloadListener1], only the
 * [DownloadListener1.taskEnd] is necessary.
 */
fun createListener1(
    taskStart: onTaskStartWithModel? = null,
    retry: onRetry? = null,
    connected: onConnected? = null,
    progress: onProgress? = null,
    taskEnd: onTaskEndWithModel?=null
): DownloadListener1 = object : DownloadListener1() {
    override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
        taskStart?.invoke(task, model)
    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {
        retry?.invoke(task, cause)
    }

    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long
    ) {
        connected?.invoke(task, blockCount, currentOffset, totalLength)
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        progress?.invoke(task, currentOffset, totalLength)
    }

    override fun taskEnd(
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        model: Listener1Assist.Listener1Model
    ) {
        taskEnd?.invoke(task, cause, realCause, model)
    }
}
