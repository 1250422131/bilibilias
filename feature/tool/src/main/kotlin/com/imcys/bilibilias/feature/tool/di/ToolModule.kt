package com.imcys.bilibilias.feature.tool.di

import com.imcys.bilibilias.feature.tool.DefaultToolComponent
import com.imcys.bilibilias.feature.tool.ToolComponent
import com.imcys.bilibilias.feature.tool.download.DefaultDownloadBottomSheetComponent
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ToolModule {

    @Binds
    fun toolComponentFactory(impl: DefaultToolComponent.Factory): ToolComponent.Factory

    @Binds
    fun downloadBottomSheetComponentFactory(
        impl: DefaultDownloadBottomSheetComponent.Factory,
    ): DownloadBottomSheetComponent.Factory
}
