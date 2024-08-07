package com.imcys.bilibilias.di

import com.imcys.bilibilias.navigation.DefaultRootComponent
import com.imcys.bilibilias.navigation.RootComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ComponentModule {

    @Binds
    fun componentRootFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}
