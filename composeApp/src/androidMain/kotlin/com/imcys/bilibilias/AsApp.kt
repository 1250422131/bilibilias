package com.imcys.bilibilias

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.util.DebugLogger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.datasource.createHttpClient

class AsApp : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(KtorNetworkFetcherFactory(createHttpClient())) }
            .apply {
                if (KmpContext.isDebug) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}