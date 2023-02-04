package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.app.BaseApplication
import io.microshow.rxffmpeg.RxFFmpegInvoke
import org.xutils.x


class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()



        handler = Handler(mainLooper)

        //xUtils初始化
        x.Ext.init(this)
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        RxFFmpegInvoke.getInstance().setDebug(true)


        context = BaseApplication.context

    }


    companion object {


        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"

        val downloadQueue: DownloadQueue = DownloadQueue()


        //——————————————————全局线程处理器——————————————————
        lateinit var handler: Handler
        //—————————————————————————————————————————————————

        //——————————————————B站视频模板——————————————————
        val videoEntry: String by lazy {
            App().getString(R.string.VideoEntry)
        }
        val videoIndex: String by lazy {
            App().getString(R.string.VideoIndex)
        }
        val bangumiEntry: String by lazy {
            App().getString(R.string.BangumiEntry)
        }
        //——————————————————部分内置需要的上下文——————————————————
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //—————————————————————————————————————————————————

    }

}