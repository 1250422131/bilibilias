package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.content.Context
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import io.microshow.rxffmpeg.RxFFmpegInvoke
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

@HiltAndroidApp
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        RxFFmpegInvoke.getInstance().setDebug(false)

        context = applicationContext()
        initTimber()

        initPlayManager()
        MMKV.initialize(this)
    }

    private fun initPlayManager() {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
