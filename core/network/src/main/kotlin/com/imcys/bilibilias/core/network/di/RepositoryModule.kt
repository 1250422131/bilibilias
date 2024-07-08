package com.imcys.bilibilias.core.network.di

import com.imcys.bilibilias.core.network.repository.DefalutLoginRepository
import com.imcys.bilibilias.core.network.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRepository(impl: DefalutLoginRepository): LoginRepository
}
