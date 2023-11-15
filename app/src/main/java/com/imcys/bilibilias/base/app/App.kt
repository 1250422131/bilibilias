package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hjq.toast.Toaster
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

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

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
