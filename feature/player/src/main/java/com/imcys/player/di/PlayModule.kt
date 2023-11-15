package com.imcys.player.di

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import com.imcys.network.constants.BILIBILI_WEB_URL
import com.imcys.network.di.BaseOkhttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayModule {
    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun provideOkhttpDataSource(
        @BaseOkhttpClient okhttpClient: OkHttpClient,
    ): DataSource.Factory = OkHttpDataSource.Factory(okhttpClient)
        .setDefaultRequestProperties(mapOf(HTTP_HEADER_REFERRER to BILIBILI_WEB_URL))
        .setCacheControl(CacheControl.FORCE_CACHE)
}

const val HTTP_HEADER_REFERRER = "Referrer"
