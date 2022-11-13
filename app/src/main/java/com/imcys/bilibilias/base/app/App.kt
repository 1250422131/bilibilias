package com.imcys.bilibilias.base.app

import android.app.Application
import android.os.Handler
import okhttp3.Cookie

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)

    }

    companion object {
        lateinit var cookies: String
        lateinit var sessdata: String
        lateinit var biliJct: String
        lateinit var handler: Handler
    }

}