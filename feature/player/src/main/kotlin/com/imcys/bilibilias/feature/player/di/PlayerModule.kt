package com.imcys.bilibilias.feature.player.di

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import com.imcys.bilibilias.core.network.api.BILIBILI_URL
import com.imcys.bilibilias.core.network.api.BROWSER_USER_AGENT
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
        .setDefaultRequestProperties(mapOf("Referrer" to BILIBILI_URL))
        .setUserAgent(BROWSER_USER_AGENT)
        .setCacheControl(CacheControl.FORCE_CACHE)
}

