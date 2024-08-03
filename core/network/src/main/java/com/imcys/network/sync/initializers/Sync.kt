package com.imcys.network.sync.initializers

import android.content.*
import androidx.work.*
import com.imcys.network.sync.workers.*

object Sync {
    // This method is initializes sync, the process that keeps the app's data current.
    // It is called from the app module's Application.onCreate() and should be only done once.
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniquePeriodicWork(
                SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
                updateCookieRequest
            )
        }
    }
}

// This name should not be changed otherwise the app may have concurrent sync requests running
internal const val SYNC_WORK_NAME = "SyncWorkName"