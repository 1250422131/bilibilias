package com.imcys.bilibilias

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.util.DebugLogger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.work.Sync
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory

class AsApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            logger<AsApplication>().error(e) { "!!! FATAL !!!" }
            defaultUEH?.uncaughtException(t, e)
        }

        initKoin {
            androidContext(this@AsApplication)
            modules(commonModules())
            workManagerFactory()
        }
        Sync.initialize(this)
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