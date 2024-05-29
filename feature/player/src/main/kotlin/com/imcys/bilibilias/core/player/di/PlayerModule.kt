package com.imcys.bilibilias.core.player.di

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {
    @androidx.annotation.OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideOkhttpDataSource(
        okhttpClient: OkHttpClient,
    ): DataSource.Factory = OkHttpDataSource.Factory(okhttpClient)
        .setDefaultRequestProperties(mapOf("Referrer" to "https://www.bilibili.com"))
        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36 Edg/125.0.0.0")
        .setCacheControl(CacheControl.FORCE_CACHE)
}