package com.imcys.bilibilias.startup

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