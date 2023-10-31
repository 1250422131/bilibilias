package com.imcys.bilibilias.common.base.app

import android.app.Application
import android.content.Context
import android.os.Handler
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.imcys.bilibilias.common.base.model.user.AsUser
import com.imcys.bilibilias.common.base.model.user.MyUserData
import javax.inject.Inject

open class BaseApplication : Application(), Configuration.Provider {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        handler = Handler(mainLooper)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {

        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"


        val asUser: AsUser
            get() = TODO()

        // ——————————————————全局线程处理器——————————————————
        lateinit var handler: Handler
            private set
        // —————————————————————————————————————————————————

        // ——————————————————B站视频模板——————————————————

        // ——————————————————部分内置需要的上下文——————————————————

        private var instance: BaseApplication? = null

        @JvmStatic
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        var mid: Long = 0
        lateinit var myUserData: MyUserData
        // —————————————————————————————————————————————————
    }
}
