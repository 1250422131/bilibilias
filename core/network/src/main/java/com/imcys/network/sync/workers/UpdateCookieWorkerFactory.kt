package com.imcys.network.sync.workers

import android.content.*
import androidx.work.*
import com.imcys.network.repository.auth.*
import javax.inject.*

class UpdateCookieWorkerFactory @Inject constructor(private val authRepository: IAuthDataSources) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker = UpdateCookieWorker(appContext, workerParameters, authRepository)

}