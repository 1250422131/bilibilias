package com.imcys.bilibilias

import android.app.Application
import android.content.Intent
import android.os.Build
import co.touchlab.kermit.Logger
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.util.DebugLogger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import com.imcys.bilibilias.core.ktor.client.createHttpClient

class AsApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Logger.e("AsApplication", e, "!!! FATAL !!!")
            defaultUEH?.uncaughtException(t, e)
        }
        StartupSet.create(AsDispatchers.applicationScope)

        initKoin {
            modules(commonModules())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(
                Intent(this, DownloadService::class.java).apply {
                    putExtra("app_name", R.string.app_name)
//                    putExtra("app_service_title_text_idle", R.string.app_service_title_text_idle)
//                    putExtra("app_service_title_text_working", R.string.app_service_title_text_working)
//                    putExtra("app_service_content_text", R.string.app_service_content_text)
//                    putExtra("app_service_stop_text", R.string.app_service_stop_text)
//                    putExtra("app_icon", R.mipmap.ic_launcher)
                    putExtra(
                        "open_activity_intent",
                        Intent(this@AsApplication, MainActivity::class.java)
                    )
                }
            )
        } else {
            startService(
                Intent(this, DownloadService::class.java).apply {

                }
            )
        }
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