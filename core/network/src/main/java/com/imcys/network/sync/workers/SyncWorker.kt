package com.imcys.network.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams) {
//    val saveRequest =
//        PeriodicWorkRequestBuilder<SaveImageToFileWorker>(1, TimeUnit.HOURS)
//            // Additional configuration
//            .build()
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}
