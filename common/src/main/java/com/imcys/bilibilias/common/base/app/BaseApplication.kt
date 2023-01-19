package com.imcys.bilibilias.common.base.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.drake.statelayout.StateConfig
import com.imcys.bilibilias.common.BuildConfig
import com.imcys.bilibilias.common.R
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.data.AppDatabase
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.Config
import com.xiaojinzi.component.impl.application.ModuleManager


open class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        context = this
        handler = Handler(mainLooper)

        //百度统计开始
        startBaiDuService()
        appDatabase = AppDatabase.getDatabase(this)

        initKComponent()

        initBRV()



    }

    private fun initKComponent() {
        Component.init(
            application = this,
            isDebug = BuildConfig.DEBUG,
            config = Config.Builder()
                .build()
        )
        // 手动加载模块
        ModuleManager.registerArr(
            "app", "common", "tool_roam",
        )
    }

    private fun initBRV() {
        StateConfig.apply {
            emptyLayout = R.layout.public_layout_empty
            errorLayout = R.layout.public_layout_error
            loadingLayout = R.layout.public_layout_loading
        }
    }


    /**
     * 百度统计
     */
    private fun startBaiDuService() {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getBoolean("baidu_statistics_type", false)) {
            StatService.setAuthorizedState(applicationContext, true)
        } else {
            StatService.setAuthorizedState(applicationContext, false)
        }
        StatService.autoTrace(applicationContext)

    }


    companion object {

        lateinit var appDatabase: AppDatabase

        const val appSecret = "3c7c5174-a6be-4093-a0df-c6fbf7371480"
        const val AppGuideVersion = "1.0"


        lateinit var cookies: String
        lateinit var sessdata: String
        lateinit var biliJct: String
        //—————————————————————————————————————————————————

        //——————————————————全局线程处理器——————————————————
        lateinit var handler: Handler
        //—————————————————————————————————————————————————

        //——————————————————APP全局数据——————————————————
        lateinit var sharedPreferences: SharedPreferences

        //——————————————————B站视频模板——————————————————
        lateinit var videoEntry: String
        lateinit var videoIndex: String
        lateinit var bangumiEntry: String

        var roamApi: String = "https://api.bilibili.com/"


        //——————————————————部分内置需要的上下文——————————————————
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var mid: Long = 0
        lateinit var myUserData: MyUserData.DataBean
        //—————————————————————————————————————————————————
    }

}