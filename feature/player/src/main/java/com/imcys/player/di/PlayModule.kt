package com.imcys.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cronet.CronetDataSource
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.network.constants.BILIBILI_URL
import com.imcys.network.constants.BROWSER_USER_AGENT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import org.chromium.net.CronetEngine
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayModule {
    @Provides
    @Singleton
    fun provideCronetEngine(@ApplicationContext context: Context): CronetEngine = CronetEngine.Builder(context)
        .enableQuic(true)
        .enableBrotli(true)
        .enableHttp2(true)
        .setStoragePath(context.cacheDir.path)
        .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISK_NO_HTTP, 1024 * 1024 * 1024L)
        .setUserAgent(BROWSER_USER_AGENT)
        .enableNetworkQualityEstimator(true)
        .build()

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun provideCronetDataSource(
        cronetEngine: CronetEngine,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): CronetDataSource.Factory = CronetDataSource.Factory(cronetEngine, ioDispatcher.asExecutor())
        .setDefaultRequestProperties(mapOf("Referrer" to BILIBILI_URL))
}
