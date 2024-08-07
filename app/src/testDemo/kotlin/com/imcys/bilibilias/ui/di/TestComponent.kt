package com.imcys.bilibilias.ui.di

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.imcys.bilibilias.navigation.RootComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestComponent {
    @Provides
    @Singleton
    fun providesTestRootComponent(rootComponentFactory: RootComponent.Factory) =
        rootComponentFactory(
            DefaultComponentContext(LifecycleRegistry()),
        )
}