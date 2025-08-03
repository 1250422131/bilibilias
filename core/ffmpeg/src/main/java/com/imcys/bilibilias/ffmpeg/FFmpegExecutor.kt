package com.imcys.bilibilias.ffmpeg

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private fun getExecutorConcurrent(): Int {
    val processors = Runtime.getRuntime().availableProcessors()
    return when {
        processors in 1..3 -> 1
        else -> (processors / 2)
    }
}

object FFmpegExecutor {

    private val currentTasks = AtomicInteger(0)
    private val maxConcurrentTasks = getExecutorConcurrent()
    private var threadId = 0
    private val executor: Executor = Executors.newFixedThreadPool(maxConcurrentTasks) {
        Thread(it, "FFmpegThread-${threadId++}").apply { isDaemon = true }
    }

    suspend fun <T> executeFFmpeg(block: () -> T): T {
        return suspendCancellableCoroutine {
            val task = wrapTask(it, block)
            val taskNum = currentTasks.incrementAndGet()
            executor.execute(task)
        }
    }

    private fun <T> wrapTask(con: CancellableContinuation<T>, block: () -> T): Runnable {
        return Runnable {
            try {
                val result = block()
                con.resumeWith(Result.success(result))
            } catch (e: Exception) {
                con.resumeWith(Result.failure(e))
            } finally {
                currentTasks.decrementAndGet()
            }
        }
    }

    val pendingTaskCount: Int
        get() = 0.coerceAtLeast(currentTasks.get() - maxConcurrentTasks)
}