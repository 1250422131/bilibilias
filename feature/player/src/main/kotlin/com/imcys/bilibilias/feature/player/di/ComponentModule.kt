package com.imcys.bilibilias.feature.player.di

import com.imcys.bilibilias.feature.player.DefaultPlayerComponent
import com.imcys.bilibilias.feature.player.PlayerComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ComponentModule {
    @Binds
    fun dialogComponentFactory(impl: DefaultPlayerComponent.Factory): PlayerComponent.Factory
}
