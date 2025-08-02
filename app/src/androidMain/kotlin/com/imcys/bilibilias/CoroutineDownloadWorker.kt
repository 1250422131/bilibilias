package com.imcys.bilibilias

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.http.downloader.HttpDownloader

class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheDataSource: MediaCacheDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {


        return Result.success()
    }

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
//        fun startUpSyncWork() = OneTimeWorkRequestBuilder<SyncWorker>()
//            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
//            .setConstraints(SyncConstraints)
//            .setInputData(SyncWorker::class.delegatedData())
//            .build()
    }
}