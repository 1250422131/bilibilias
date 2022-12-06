package com.imcys.bilibilias.base.app

import android.app.Application
import android.os.Handler
import com.imcys.bilibilias.base.utils.DownloadQueue
import org.xutils.x

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
        //xUtils初始化
        x.Ext.init(this)
        x.Ext.setDebug(false)
    }



    companion object {
        val downloadQueue: DownloadQueue = DownloadQueue()
        lateinit var cookies: String
        lateinit var sessdata: String
        lateinit var biliJct: String
        lateinit var handler: Handler
        var mid: Long = 0L
    }

}