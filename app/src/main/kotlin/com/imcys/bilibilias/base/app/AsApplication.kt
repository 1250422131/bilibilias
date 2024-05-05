package com.imcys.bilibilias.base.app

import coil.ImageLoader
import coil.ImageLoaderFactory
import com.hjq.toast.Toaster
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.util.ProfileVerifierLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class AsApplication : BaseApplication(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var profileVerifierLogger: ProfileVerifierLogger

    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)

        profileVerifierLogger()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
