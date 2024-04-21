package com.imcys.bilibilias.core.download.di

import android.content.Context
import androidx.tracing.trace
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.connection.DownloadOkHttpConnection
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DownloadModule {

    @Provides
    @Singleton
    fun okDownload(
        okHttpClient: dagger.Lazy<OkHttpClient>,
        @ApplicationContext application: Context,
        executorService: ExecutorService,
    ): OkDownload = trace("OkDownload") {
        OkDownload.Builder(application)
            .connectionFactory(
                DownloadOkHttpConnection.Factory().setBuilder(okHttpClient.get().newBuilder())
            )
            .downloadDispatcher(DownloadDispatcher(executorService))
            .build().also {
                OkDownload.setSingletonInstance(it)
            }
    }
}
