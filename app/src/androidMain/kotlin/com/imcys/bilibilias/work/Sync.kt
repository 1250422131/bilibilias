package com.imcys.bilibilias.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
    // This method is initializes sync, the process that keeps the app's data current.
    // It is called from the app module's Application.onCreate() and should be only done once.
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.KEEP,
                CoroutineDownloadWorker.startWork(),
            )
        }
    }
}

internal const val WORK_NAME = "WorkName"