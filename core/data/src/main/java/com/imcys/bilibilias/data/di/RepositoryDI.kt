package com.imcys.bilibilias.data.di

import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.RiskManagementRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.datastore.userAppSettingsStore
import com.imcys.bilibilias.datastore.userUserStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { AppSettingsRepository(androidContext().userAppSettingsStore) }
    single { QRCodeLoginRepository(get(), get(), get(), get()) }
    single { RiskManagementRepository(get(), get()) }
    single { UserInfoRepository(get(), get(),  get(), get()) }
    single { VideoInfoRepository(get(), get(),  get(),get()) }
    single { DownloadTaskRepository(get (),get (),get ()) }
}