package com.liulishuo.okdownload.di

import android.content.Context
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.connection.DownloadOkHttpConnection
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
interface OkDownloadModule {

    @Provides
    @Singleton
    fun initializeOkDownload(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        executorService: ExecutorService,
    ): OkDownload = OkDownload.Builder(context)
        .connectionFactory(
            DownloadOkHttpConnection.Factory().setClient(okHttpClient),
        )
        .executor(executorService)
        .build()
        .also(OkDownload::setSingletonInstance)
}
