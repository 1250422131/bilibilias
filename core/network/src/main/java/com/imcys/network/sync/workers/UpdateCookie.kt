package com.imcys.network.sync.workers

import android.content.*
import androidx.hilt.work.*
import androidx.work.*
import com.imcys.network.repository.auth.*
import dagger.assisted.*
import io.github.aakira.napier.*
import java.util.concurrent.*

internal val updateCookieRequest =
    PeriodicWorkRequestBuilder<UpdateCookieWorker>(1, TimeUnit.DAYS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

@HiltWorker
class UpdateCookieWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: IAuthDataSources
) : CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {
        return try {
            authRepository.cookieRefreshChain()
            Result.success()
        } catch (e: Exception) {
            Napier.d(e, "UpdateCookieWorker") { "更新 cookie 发生错误" }
            Result.failure()
        }
    }
}