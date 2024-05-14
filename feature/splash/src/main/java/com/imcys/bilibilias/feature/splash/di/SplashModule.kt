package com.imcys.bilibilias.feature.splash.di

import com.imcys.bilibilias.feature.splash.DefaultSplashComponent
import com.imcys.bilibilias.feature.splash.SplashComponent
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
