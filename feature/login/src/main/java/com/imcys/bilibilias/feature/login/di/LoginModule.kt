package com.imcys.bilibilias.feature.login.di

import com.imcys.bilibilias.feature.login.component.DefaultLoginComponent
import com.imcys.bilibilias.feature.login.component.LoginComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {

    @Binds
    fun componentFactory(impl: DefaultLoginComponent.Factory): LoginComponent.Factory
}
