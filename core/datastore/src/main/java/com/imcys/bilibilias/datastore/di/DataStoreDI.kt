package com.imcys.bilibilias.datastore.di

import com.imcys.bilibilias.datastore.googlePlayerStore
import com.imcys.bilibilias.datastore.source.AppSettingSource
import com.imcys.bilibilias.datastore.source.GooglePlaySource
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.datastore.userAppSettingsStore
import com.imcys.bilibilias.datastore.userUserStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        UsersDataSource(androidContext().userUserStore)
    }
    single {
        GooglePlaySource(androidContext().googlePlayerStore)
    }

    single {
        AppSettingSource(androidContext().userAppSettingsStore)
    }
}