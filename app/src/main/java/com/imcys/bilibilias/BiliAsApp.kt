package com.imcys.bilibilias

import android.app.Application
import android.content.Context
import com.bilias.crash.ACRAUtils
import com.imcys.common.appinitializer.AppInitializersProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BiliAsApp : Application() {
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
}
