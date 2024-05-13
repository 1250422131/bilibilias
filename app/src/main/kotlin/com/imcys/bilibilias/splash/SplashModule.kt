package com.imcys.bilibilias.splash

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashModule {

    @Binds
    fun componentFactory(impl: DefaultSplashComponent.Factory): SplashComponent.Factory
}
