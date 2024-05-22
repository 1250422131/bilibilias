package com.liulishuo.okdownload.di

import android.content.Context
import androidx.startup.Initializer
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.connection.DownloadOkHttpConnection
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher

class OkDownloadInitializer : Initializer<OkDownload> {
    override fun create(context: Context): OkDownload {
        val resolve = InitializerEntryPoint.resolve(context)
        return OkDownload.Builder(context)
            .connectionFactory(
                DownloadOkHttpConnection.Factory().setClient(resolve.injectOkhttp())
            )
            .downloadDispatcher(DownloadDispatcher(resolve.injectExecutorService()))
            .build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
