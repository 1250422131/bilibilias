package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.repository.FavoritesRepository
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.repository.WbiKeyRepository
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
    fun provideFavoritesRepository(httpClient: HttpClient) = FavoritesRepository(httpClient)

    @Provides
    @Singleton
    fun provideVideoRepository(httpClient: HttpClient) = VideoRepository(httpClient)
    @Provides
    @Singleton
    fun provideWbiKey(httpClient: HttpClient) = WbiKeyRepository(httpClient)


}
