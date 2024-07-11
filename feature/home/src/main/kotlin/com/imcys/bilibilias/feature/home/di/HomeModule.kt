package com.imcys.bilibilias.feature.home.di

import com.imcys.bilibilias.feature.home.DefaultHomeComponent
import com.imcys.bilibilias.feature.home.HomeComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {

    @Binds
    fun componentFactory(impl: DefaultHomeComponent.Factory): HomeComponent.Factory
}
