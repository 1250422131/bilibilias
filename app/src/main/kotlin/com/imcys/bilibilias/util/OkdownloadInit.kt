package com.imcys.bilibilias.util

import android.content.Context
import androidx.compose.ui.util.trace
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.OkDownloadProvider
import com.liulishuo.okdownload.core.connection.DownloadOkHttpConnection
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class OkdownloadInit @Inject constructor(
    @ApplicationContext private val application: Context,
    private val okHttpClient: dagger.Lazy<OkHttpClient>,
    private val executorService: ExecutorService,
) {
    operator fun invoke() {
        trace("OkDownloadInit") {
            OkDownloadProvider.context = application
            OkDownload.Builder(application)
                .connectionFactory(
                    DownloadOkHttpConnection.Factory().setClient(okHttpClient.get())
                )
                .downloadDispatcher(DownloadDispatcher(executorService))
                .build().also {
                    OkDownload.setSingletonInstance(it)
                }
        }
    }
}
