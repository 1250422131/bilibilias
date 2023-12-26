package com.imcys.bilibilias

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.bilias.crash.ACRAUtils
import com.imcys.common.appinitializer.AppInitializersProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider
@HiltAndroidApp
class BiliAsApp : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    @Inject
    lateinit var mAppInitializer: AppInitializersProvider
    override fun onCreate() {
        super.onCreate()
        mAppInitializer.startInit()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ACRAUtils.init()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
