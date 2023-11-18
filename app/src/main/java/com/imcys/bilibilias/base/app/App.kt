package com.imcys.bilibilias.base.app

import android.app.Application
import android.content.Context
import com.hjq.toast.Toaster
import com.imcys.common.appinitializer.AppInitializersProvider
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    init {
        context = this
    }

    @Inject
    lateinit var mAppInitializer: AppInitializersProvider
    override fun onCreate() {
        super.onCreate()
        mAppInitializer.startInit()
        initTimber()

        initPlayManager()
        MMKV.initialize(this)
        Toaster.init(this)
    }

    private fun initPlayManager() {
        // PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"

        lateinit var context: Context
    }
}
