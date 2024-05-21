package com.imcys.bilibilias

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.hjq.toast.Toaster
import com.imcys.bilibilias.util.OkdownloadInit
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import javax.inject.Inject

@HiltAndroidApp
class AsApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var bugly: Bugly

    @Inject
    lateinit var okdownload: OkdownloadInit

    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)
        Napier.base(DebugAntilog())
        bugly()
        okdownload()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
