package com.imcys.bilibilias.core.analytics.di

import com.imcys.bilibilias.core.analytics.AnalyticsHelper
import com.imcys.bilibilias.core.analytics.StubAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    internal abstract fun bindsSyncStatusMonitor(analytics: StubAnalyticsHelper): AnalyticsHelper
}
