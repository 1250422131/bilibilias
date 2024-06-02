package com.imcys.bilibilias.feature.settings.di

import com.imcys.bilibilias.feature.settings.DefaultSettingsComponent
import com.imcys.bilibilias.feature.settings.SettingsComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {

    @Binds
    fun componentFactory(impl: DefaultSettingsComponent.Factory): SettingsComponent.Factory
}
