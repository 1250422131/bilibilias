package com.imcys.bilibilias.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

object ExecutorUtil {
    val executorService = Dispatchers.IO.asExecutor().asNonTerminatingExecutorService()
}

fun Executor.asNonTerminatingExecutorService(): ExecutorService =
    object : Executor by this, AbstractExecutorService() {
        override fun shutdown() {
            throw UnsupportedOperationException("shutdown is not implemented")
        }

        override fun shutdownNow(): MutableList<Runnable> {
            throw UnsupportedOperationException("shutdownNow is not implemented")
        }

        override fun isShutdown(): Boolean = false

        override fun isTerminated(): Boolean = false

        override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean = false
    }
