package com.imcys.bilibilias

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun KoinApplication.platformModule(): Module = module {
    workManagerFactory()
    workerOf(::CoroutineDownloadWorker)
}