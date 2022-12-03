package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import com.imcys.bilibilias.base.utils.VideoDownloadTask
import okhttp3.Cookie
import org.xutils.BuildConfig
import org.xutils.x
import kotlin.properties.Delegates

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
        //xUtils初始化
        x.Ext.init(this)
        x.Ext.setDebug(false)
    }



    companion object {
        lateinit var cookies: String
        lateinit var sessdata: String
        lateinit var biliJct: String
        lateinit var handler: Handler
        var mid: Long = 0L

        lateinit var videoDownloadTask: VideoDownloadTask
    }

}