package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideVideoRepository(httpClient: HttpClient) = VideoRepository(httpClient)
}
