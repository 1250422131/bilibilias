package com.imcys.bilibilias.sync.status

import android.util.Log
import javax.inject.Inject

private const val TAG = "StubSyncSubscriber"

/**
 * Stub implementation of [SyncSubscriber]
 */
class StubSyncSubscriber @Inject constructor() : SyncSubscriber {
    override suspend fun subscribe() {
        Log.d(TAG, "Subscribing to sync")
    }
}
