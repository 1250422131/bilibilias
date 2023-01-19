package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import com.drake.brv.utils.BRV
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.tool_roam.BR
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

        //初始化
        BRV.modelId = BR.data
    }


    companion object{


        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"

        val downloadQueue: DownloadQueue = DownloadQueue()




        //——————————————————全局线程处理器——————————————————
        lateinit var handler: Handler
        //—————————————————————————————————————————————————

        //——————————————————APP全局数据——————————————————
        lateinit var sharedPreferences: SharedPreferences

        //——————————————————B站视频模板——————————————————
        lateinit var videoEntry: String
        lateinit var videoIndex: String
        lateinit var bangumiEntry: String

        //——————————————————部分内置需要的上下文——————————————————
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //—————————————————————————————————————————————————

    }

}