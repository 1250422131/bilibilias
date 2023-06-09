package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.tool_log_export.BR
import dagger.hilt.android.HiltAndroidApp
import io.microshow.rxffmpeg.RxFFmpegInvoke
import org.xutils.x

@HiltAndroidApp
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //xUtils初始化
        x.Ext.init(this)
        x.Ext.setDebug(false) // 是否输出debug日志, 开启debug会影响性能.
        RxFFmpegInvoke.getInstance().setDebug(false)

        //BRV初始化
        initBRV()

        context = BaseApplication.context

    }

    private fun initBRV() {
        // 初始化BindingAdapter的默认绑定ID, 如果不使用DataBinding并不需要初始化
        BRV.modelId = BR.data

        StateConfig.apply {
            emptyLayout = com.imcys.bilibilias.common.R.layout.public_layout_empty
            errorLayout = com.imcys.bilibilias.common.R.layout.public_layout_error
            loadingLayout = com.imcys.bilibilias.common.R.layout.public_layout_loading
        }
    }


    companion object {


        const val appSecret = BuildConfig.APP_CENTER_SECRET
        const val AppGuideVersion = "1.0"
        val downloadQueue: DownloadQueue by lazy { DownloadQueue() }

        //—————————————————————————————————————————————————

        //——————————————————B站视频模板——————————————————
        val videoEntry: String by lazy {
            context.getString(R.string.VideoEntry)
        }
        val videoIndex: String by lazy {
            context.getString(R.string.VideoIndex)
        }
        val bangumiEntry: String by lazy {
            context.getString(R.string.BangumiEntry)
        }

        //——————————————————部分内置需要的上下文——————————————————
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //—————————————————————————————————————————————————

    }

}