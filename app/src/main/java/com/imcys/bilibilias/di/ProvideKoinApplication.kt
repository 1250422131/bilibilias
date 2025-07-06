package com.imcys.bilibilias.di

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.data.di.repositoryModule
import com.imcys.bilibilias.database.di.databaseModule
import com.imcys.bilibilias.datastore.di.dataStoreModule
import com.imcys.bilibilias.network.di.netWorkModule
import org.koin.compose.KoinApplication


@Composable
fun ProvideKoinApplication(content: @Composable () -> Unit) {
    KoinApplication(application = {
        modules(
            dataStoreModule,
            netWorkModule,
            repositoryModule,
            databaseModule,
            appModule,
        )
    }) {
        content()
    }
}