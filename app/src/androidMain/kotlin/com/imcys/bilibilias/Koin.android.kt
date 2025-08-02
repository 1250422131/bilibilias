package com.imcys.bilibilias

import androidx.work.CoroutineWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

actual fun KoinApplication.platformModule(): Module = module {
    workerOf(::CoroutineDownloadWorker) binds arrayOf(CoroutineWorker::class)
}