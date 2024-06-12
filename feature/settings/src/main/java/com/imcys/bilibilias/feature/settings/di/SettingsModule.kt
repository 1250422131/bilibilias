package com.imcys.bilibilias.feature.settings.di

import com.imcys.bilibilias.feature.settings.component.DefaultSettingsComponent
import com.imcys.bilibilias.feature.settings.component.SettingsComponent
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
