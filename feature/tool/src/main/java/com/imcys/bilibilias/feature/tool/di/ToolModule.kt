package com.imcys.bilibilias.feature.tool.di

import com.imcys.bilibilias.feature.tool.DefaultToolComponent
import com.imcys.bilibilias.feature.tool.ToolComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ToolModule {

    @Binds
    fun componentFactory(impl: DefaultToolComponent.Factory): ToolComponent.Factory
}
