package com.imcys.bilibilias.feature.download.di

import com.imcys.bilibilias.feature.download.DefaultDownloadComponent
import com.imcys.bilibilias.feature.download.DownloadComponent
import com.imcys.bilibilias.feature.download.sheet.DefaultDialogComponent
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DownloadModule {

    @Binds
    fun downloadComponentFactory(impl: DefaultDownloadComponent.Factory): DownloadComponent.Factory

    @Binds
    fun dialogComponentFactory(impl: DefaultDialogComponent.Factory): DialogComponent.Factory
}
