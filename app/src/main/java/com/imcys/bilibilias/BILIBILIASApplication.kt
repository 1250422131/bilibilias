package com.imcys.bilibilias

import android.app.Application
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.common.data.CommonBuildConfig
import com.imcys.bilibilias.common.utils.baiduAnalyticsSafe
import com.imcys.bilibilias.data.di.repositoryModule
import com.imcys.bilibilias.database.di.databaseModule
import com.imcys.bilibilias.datastore.di.dataStoreModule
import com.imcys.bilibilias.di.appModule
import com.imcys.bilibilias.network.di.netWorkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BILIBILIASApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 全局异常捕获
        // AppCrashHandler.instance.init(this)
        initBuildConfig()
        // 初始化百度统计
        baiduAnalyticsSafe {
            StatService.init(this,BuildConfig.BAIDU_STAT_ID,getString(R.string.app_channel))
        }
        // Koin依赖注入
        startKoin {
            androidContext(this@BILIBILIASApplication)
            modules(
                dataStoreModule,
                netWorkModule,
                repositoryModule,
                databaseModule,
                appModule,
            )
        }
    }

    private fun initBuildConfig() {
        CommonBuildConfig.enabledAnalytics = BuildConfig.ENABLED_ANALYTICS
    }
}