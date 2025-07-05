package com.imcys.bilibilias

import android.app.Application
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
}