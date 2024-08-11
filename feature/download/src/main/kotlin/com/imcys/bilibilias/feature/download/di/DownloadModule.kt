package com.imcys.bilibilias.feature.download.di

import com.imcys.bilibilias.feature.download.component.DefaultDownloadComponent
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DownloadModule {

    @Binds
    fun downloadComponentFactory(impl: DefaultDownloadComponent.Factory): DownloadComponent.Factory
}
