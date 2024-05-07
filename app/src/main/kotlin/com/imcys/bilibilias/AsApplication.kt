package com.imcys.bilibilias

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.hjq.toast.Toaster
import com.imcys.bilibilias.util.AppCenter
import com.imcys.bilibilias.util.OkdownloadInit
import com.imcys.bilibilias.util.ProfileVerifierLogger
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import javax.inject.Inject

@HiltAndroidApp
class AsApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var profileVerifierLogger: ProfileVerifierLogger

    @Inject
    lateinit var appCenter: AppCenter

    @Inject
    lateinit var okdownloadInit: OkdownloadInit

    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)
        Napier.base(DebugAntilog())
        profileVerifierLogger()
        appCenter(this)
        okdownloadInit()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
