package com.imcys.bilibilias.base.app

import android.annotation.SuppressLint
import android.content.Context
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.tool_log_export.BR
import dagger.hilt.android.HiltAndroidApp
import io.microshow.rxffmpeg.RxFFmpegInvoke

@HiltAndroidApp
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        RxFFmpegInvoke.getInstance().setDebug(false)

        // BRV初始化
        initBRV()

        context = applicationContext()
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

        const val AppGuideVersion = "1.0"

        // —————————————————————————————————————————————————

        val videoIndex: String by lazy {
            context.getString(R.string.VideoIndex)
        }
        val bangumiEntry: String by lazy {
            context.getString(R.string.BangumiEntry)
        }

        // ——————————————————部分内置需要的上下文——————————————————
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        // —————————————————————————————————————————————————
    }
}
