package com.imcys.bilibilias.common.base.app

import android.app.Application
import android.content.Context
import android.os.Handler
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.model.user.AsUser
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.tencent.mmkv.MMKV
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

open class BaseApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        handler = Handler(mainLooper)

        initMMKV()
        initNapier()
    }

    private fun initNapier() {
        Napier.base(DebugAntilog())
    }

    private fun initMMKV() {
        MMKV.initialize(this)
        dataKv = MMKV.mmkvWithID("data")
    }

    companion object {

        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"

        // 全局应用数据的MMKV
        lateinit var dataKv: MMKV
            private set
        val asUser: AsUser
            get() = run {
                val kv = dataKv
                AsUser.apply {
                    cookie = kv.decodeString(COOKIES, "")!!
                    mid = kv.decodeLong("mid", 0)
                    asCookie = kv.decodeString("as_cookie", "")!!
                }
            }

        // ——————————————————全局线程处理器——————————————————
        lateinit var handler: Handler
            private set

        private var instance: BaseApplication? = null

        @JvmStatic
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        lateinit var myUserData: MyUserData.DataBean
        // —————————————————————————————————————————————————
    }
}
