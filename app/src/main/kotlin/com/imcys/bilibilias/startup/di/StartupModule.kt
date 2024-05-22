package com.imcys.bilibilias.startup.di

import com.imcys.bilibilias.startup.DefaultStartupComponent
import com.imcys.bilibilias.startup.StartupComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StartupModule {

    @Binds
    fun componentFactory(impl: DefaultStartupComponent.Factory): StartupComponent.Factory
}