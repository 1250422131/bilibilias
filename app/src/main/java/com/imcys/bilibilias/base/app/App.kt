package com.imcys.bilibilias.base.app

import com.imcys.bilibilias.common.base.app.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import io.microshow.rxffmpeg.RxFFmpegInvoke

@HiltAndroidApp
class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        RxFFmpegInvoke.getInstance().setDebug(false)
    }
}
