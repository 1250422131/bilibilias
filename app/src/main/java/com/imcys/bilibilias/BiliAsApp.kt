package com.imcys.bilibilias

import android.app.*
import android.content.*
import androidx.work.*
import coil.*
import com.bilias.crash.*
import com.imcys.common.appinitializer.*
import com.imcys.network.sync.initializers.*
import com.imcys.network.sync.workers.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltAndroidApp
class BiliAsApp : Application(), ImageLoaderFactory, Configuration.Provider {
    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    @Inject
    lateinit var hiltWorkerFactory: UpdateCookieWorkerFactory

    @Inject
    lateinit var mAppInitializer: AppInitializersProvider
    override fun onCreate() {
        super.onCreate()
        mAppInitializer.startInit()
        Sync.initialize(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ACRAUtils.init()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setExecutor(Dispatchers.IO.asExecutor())
            .setWorkerFactory(hiltWorkerFactory)
            .build()

}
