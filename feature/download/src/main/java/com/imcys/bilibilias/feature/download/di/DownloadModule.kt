package com.imcys.bilibilias.feature.download.di

import com.imcys.bilibilias.feature.download.DefaultDownloadComponent
import com.imcys.bilibilias.feature.download.DownloadComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DownloadModule {

    @Binds
    fun componentFactory(impl: DefaultDownloadComponent.Factory): DownloadComponent.Factory
}
